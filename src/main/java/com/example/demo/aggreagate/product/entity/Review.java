package com.example.demo.aggreagate.product.entity;

import com.example.demo.aggreagate.member.entity.Member;
import com.example.demo.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Product product;

    private String title;

    @Column(length = 300)
    private String content;
    @Column(length = 1000)
    private String image;
    private int rate;

}
