package com.ll.ShinChekBang.boundedContext.book.entity;

import com.ll.ShinChekBang.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Book extends BaseEntity {
    private String title;
    private String author;
    private Integer price;
}
