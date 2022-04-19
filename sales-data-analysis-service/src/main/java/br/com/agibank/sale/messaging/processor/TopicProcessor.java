package br.com.agibank.sale.messaging.processor;

public interface TopicProcessor<T> {

    void process(T entity) throws Exception;

}