package com.example.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ImageData")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;

    @Lob
    @Column(name = "imagedata", length = 999999999)
    private byte[] imageData;
}
