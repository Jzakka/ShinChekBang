package com.ll.ShinChekBang.base.file.repository;

import com.ll.ShinChekBang.base.file.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<UploadFile, Long> {
}
