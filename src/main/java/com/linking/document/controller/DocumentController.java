package com.linking.document.controller;

import com.linking.document.dto.DocumentOrderReq;
import com.linking.document.dto.DocumentRes;
import com.linking.document.service.DocumentService;
import com.linking.global.ResponseHandler;
import com.linking.group.dto.GroupRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.PUT})
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET})
    public ResponseEntity<Object> getDocuments(@PathVariable("id") Long projectId) {
        List<GroupRes> documentRes = documentService.findAllDocuments(projectId);
        if (documentRes == null)
            return ResponseHandler.generateInternalServerErrorResponse();
        return ResponseHandler.generateOkResponse(documentRes);
    }

    @PutMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.PUT})
    public ResponseEntity<Object> putDocument(@RequestBody @Valid DocumentOrderReq req) {
        try {
            documentService.updateDocumentsOrder(req);
            return ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, "true");
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, null);
        }
    }
}
