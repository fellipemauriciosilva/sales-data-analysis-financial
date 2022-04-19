package br.com.agibank.batch.watcher;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

import br.com.agibank.batch.domain.Event;
import br.com.agibank.batch.domain.KeyOriginAndEvent;
import br.com.agibank.batch.domain.ServiceWalkAndRegisterWrapper;



public class FileWatcher implements Closeable {
	
	private static final int MAX_THREADS = 10;
	private static final long SLEEP_WATCHER = 1L;
	private volatile AtomicBoolean STARTED;
	private final Map<WatchKey, Path> keys;
	private final ExecutorService executorService;
	private final Map<KeyOriginAndEvent, List<BiConsumer<Event, String>>> fileConsumerMapRegister;
	private final Map<KeyOriginAndEvent, ServiceWalkAndRegisterWrapper> watchServiceMap;

	public FileWatcher(ExecutorService executorService) throws IOException {
		this.STARTED = new AtomicBoolean(TRUE);
		this.keys = Collections.synchronizedMap(new HashMap());
		this.fileConsumerMapRegister = Collections.synchronizedMap(new HashMap());
		this.watchServiceMap = Collections.synchronizedMap(new HashMap());
		this.executorService = executorService;
	}

	public FileWatcher() throws IOException {
		this.STARTED = new AtomicBoolean(TRUE);
		this.keys = Collections.synchronizedMap(new HashMap());
		this.fileConsumerMapRegister = Collections.synchronizedMap(new HashMap());
		this.watchServiceMap = Collections.synchronizedMap(new HashMap());
		this.executorService = Executors.newFixedThreadPool(MAX_THREADS);
	}

	public void registerConsumerCreateEvent(File origin, BiConsumer<Event, String> fileConsumer) {
		this.registerConsumerWithoutWalkAndRegisterDirectories(origin, fileConsumer, ENTRY_CREATE);
	}

	public void registerConsumerModifyEvent(File origin, BiConsumer<Event, String> fileConsumer) {
		this.registerConsumerWithoutWalkAndRegisterDirectories(origin, fileConsumer, ENTRY_MODIFY);
	}

	public void registerConsumerDeleteEvent(File origin, BiConsumer<Event, String> fileConsumer) {
		this.registerConsumerWithoutWalkAndRegisterDirectories(origin, fileConsumer, ENTRY_DELETE);
	}

	public void registerConsumerOverflowEvent(File origin, BiConsumer<Event, String> fileConsumer) {
		this.registerConsumerWithoutWalkAndRegisterDirectories(origin, fileConsumer, OVERFLOW);
	}

	public void registerConsumerAllEvents(File origin, BiConsumer<Event, String> fileConsumer) {
		this.registerConsumerWithoutWalkAndRegisterDirectories(origin, fileConsumer, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
	}

	public void registerConsumerWithWalkAndRegisterDirectories(File origin,
			BiConsumer<Event, String> fileConsumer, WatchEvent.Kind<?>... events) {
		this.registerConsumer(origin, fileConsumer, TRUE, events);
	}

	public void registerConsumerWithoutWalkAndRegisterDirectories(File origin,
			BiConsumer<Event, String> fileConsumer, WatchEvent.Kind<?>... events) {
		this.registerConsumer(origin, fileConsumer, FALSE, events);
	}

	public void registerConsumer(File origin, BiConsumer<Event, String> fileConsumer,
			Boolean walkAndRegisterDirectories, WatchEvent.Kind<?>... events) {
		if (!origin.exists()) {
			origin.mkdir();
		}

		if (!origin.isDirectory()) {
			throw new RuntimeException(origin.getAbsolutePath() + " is not a directory.");
		} else {
			Path path = Paths.get(origin.getAbsolutePath());
			Arrays.asList(events).stream().forEach((event) -> {
				try {
					WatchService watchService = path.getFileSystem().newWatchService();
					KeyOriginAndEvent watchKeyOriginAndEvent = new KeyOriginAndEvent(origin.getAbsolutePath(),
							event);
					this.watchServiceMap.put(watchKeyOriginAndEvent,
							new ServiceWalkAndRegisterWrapper(watchService, walkAndRegisterDirectories));
					this.walkAndRegisterDirectories(watchService, path, event);
					if (this.fileConsumerMapRegister.containsKey(watchKeyOriginAndEvent)) {
						this.fileConsumerMapRegister.get(watchKeyOriginAndEvent).add(fileConsumer);
					} else {
						List<BiConsumer<Event, String>> list = new ArrayList();
						list.add(fileConsumer);
						this.fileConsumerMapRegister.put(watchKeyOriginAndEvent, list);
					}

				} catch (IOException var10) {
					throw new RuntimeException(
							"m=registerConsumer, fileConsumer=" + fileConsumer + ", walkAndRegisterDirectories="
									+ walkAndRegisterDirectories + "watcherEvents=" + Arrays.toString(events),
							var10);
				}
			});
		}
	}

	public boolean unregister(File origin, BiConsumer<Event, String> fileConsumer, WatchEvent.Kind<?> event) {
		List<BiConsumer<Event, String>> consumerList = (List) this.fileConsumerMapRegister
				.get(new KeyOriginAndEvent(origin.getAbsolutePath(), event));
		return consumerList != null ? consumerList.remove(fileConsumer) : false;
	}

	public void start() {
		this.STARTED.set(TRUE);
		this.executorService.submit(() -> {
			while (this.STARTED.get()) {
				this.processEvent();

				try {
					TimeUnit.SECONDS.sleep(SLEEP_WATCHER);
				} catch (InterruptedException var2) {
					throw new RuntimeException(var2);
				}
			}

		});
	}

	private void processEvent() {
		this.watchServiceMap.entrySet().stream().forEach((entry) -> {
			this.executorService.execute(() -> {
				ServiceWalkAndRegisterWrapper wrapper = (ServiceWalkAndRegisterWrapper) entry.getValue();

				try {
					WatchKey key = wrapper.getWatchService().take();
					Path dir = this.createPath(key);
					this.poolingEvents(wrapper.getWatchService(), key, dir, wrapper.getWalkAndRegisterDirectories());
					this.checkValid(key);
				} catch (InterruptedException var5) {
					throw new RuntimeException("m=processEvent, step=\"teke directory\"", var5);
				}
			});
		});
	}

	private Path createPath(WatchKey key) {
		Path dir = (Path) this.keys.get(key);
		if (dir == null) {
			throw new RuntimeException("m=processEvent, step=\"verify directory\" msg=\"WatchKey not recognized\"");
		} else {
			return dir;
		}
	}

	private void checkValid(WatchKey key) {
		boolean valid = key.reset();
		if (!valid) {
			this.keys.remove(key);
		}

	}

	private void poolingEvents(WatchService watchService, WatchKey key, Path dir, Boolean walkAndRegisterDirectories) {
		key.pollEvents().forEach((event) -> {
			WatchEvent.Kind<?> kind = event.kind();
			Path name = (Path) event.context();
			Path child = Paths.get(dir.toString() + File.separatorChar + name.toString());
			this.dispacherEvent(dir.toFile().getAbsolutePath(), kind, child);
			if (kind == ENTRY_CREATE && walkAndRegisterDirectories
					&& Files.isDirectory(child, new LinkOption[0])) {
				try {
					this.walkAndRegisterDirectories(watchService, child);
				} catch (IOException var9) {
					throw new RuntimeException("m=processEvent, step=\"kind \"", var9);
				}
			}

		});
	}

	private void dispacherEvent(String origin, WatchEvent.Kind<?> kind, Path child) {
		Event event = new Event(origin, kind, child);
		KeyOriginAndEvent watchKeyOriginAndEvent = new KeyOriginAndEvent(origin, kind);
		this.fileConsumerMapRegister.get(watchKeyOriginAndEvent).stream().forEach((action) -> {
			String uuid = UUID.randomUUID().toString();
			action.accept(event, uuid);
		});
	}

	private void registerDirectory(WatchService watchService, Path dir, WatchEvent.Kind<?>... events)
			throws IOException {
		WatchKey key = dir.register(watchService, events);
		this.keys.put(key, dir);
	}

	private void walkAndRegisterDirectories(final WatchService watchService, Path path,
			final WatchEvent.Kind<?>... events) throws IOException {
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				FileWatcher.this.registerDirectory(watchService, dir, events);
				return CONTINUE;
			}
		});
	}

	public void close() throws IOException {
		this.STARTED.set(FALSE);
	}

}
