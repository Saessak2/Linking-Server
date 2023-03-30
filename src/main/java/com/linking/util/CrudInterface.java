package com.linking.util;


public interface CrudInterface <Request, Response>{
    Message<Response> create(Message<Request> request);
    Message<Response> read(Long id);
    Message<Response> update(Message<Request> request);
    Message delete(Long id);
}
