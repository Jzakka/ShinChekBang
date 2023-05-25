package com.ll.ShinChekBang.boundedContext.category.entity;

import com.ll.ShinChekBang.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class ParentCategory extends BaseEntity {
    @Column(unique = true)
    private String name;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentCategory", cascade = CascadeType.PERSIST)
    @Builder.Default
    @ToString.Exclude
    List<Category> childCategories = new ArrayList<>();
}
