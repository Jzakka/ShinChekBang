package com.ll.ShinChekBang.base.file.controller;

import com.ll.ShinChekBang.base.file.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileStore fileStore;

    @GetMapping("/image/{image}")
    public Resource imageSource(@PathVariable String image) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(image));
    }
}
