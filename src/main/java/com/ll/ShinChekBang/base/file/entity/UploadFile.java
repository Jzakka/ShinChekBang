package com.ll.ShinChekBang.base.file.entity;

import com.ll.ShinChekBang.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class UploadFile extends BaseEntity {
    private String uploadFileName;
    private String storeFileName;
}
