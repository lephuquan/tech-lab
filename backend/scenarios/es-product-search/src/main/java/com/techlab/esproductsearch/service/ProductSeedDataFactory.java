package com.techlab.esproductsearch.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Component;

import com.techlab.esproductsearch.domain.ProductDocument;

@Component
public class ProductSeedDataFactory {

    public List<ProductDocument> buildDefaultCatalog() {
        Instant now = Instant.now();
        return List.of(
                new ProductDocument(
                        "P-1001",
                        "Apple iPhone 15 128GB",
                        "Smartphone cao cap, camera on dinh, hieu nang tot cho da so nhu cau.",
                        "smartphone",
                        List.of("apple", "iphone", "ios", "camera"),
                        new BigDecimal("19990000"),
                        true,
                        now.minusSeconds(1_200L)
                ),
                new ProductDocument(
                        "P-1002",
                        "Samsung Galaxy S24",
                        "Dien thoai Android flagship, man hinh sang va pin ben.",
                        "smartphone",
                        List.of("samsung", "android", "amoled"),
                        new BigDecimal("18990000"),
                        true,
                        now.minusSeconds(1_100L)
                ),
                new ProductDocument(
                        "P-1003",
                        "Logitech MX Master 3S",
                        "Chuot khong day cho developer, do ben cao va click em.",
                        "accessory",
                        List.of("mouse", "logitech", "productivity"),
                        new BigDecimal("2490000"),
                        true,
                        now.minusSeconds(1_000L)
                ),
                new ProductDocument(
                        "P-1004",
                        "Keychron K8 Pro",
                        "Ban phim co khong day, hot-swap, phu hop lap trinh vien.",
                        "accessory",
                        List.of("keyboard", "keychron", "mechanical"),
                        new BigDecimal("2990000"),
                        true,
                        now.minusSeconds(900L)
                ),
                new ProductDocument(
                        "P-1005",
                        "Sony WH-1000XM5",
                        "Tai nghe chong on chu dong, ngon cho di chuyen va lam viec.",
                        "audio",
                        List.of("sony", "headphone", "noise-cancelling"),
                        new BigDecimal("7490000"),
                        false,
                        now.minusSeconds(800L)
                ),
                new ProductDocument(
                        "P-1006",
                        "Dell XPS 13",
                        "Laptop gon nhe, phu hop cho cong viec van phong va lap trinh.",
                        "laptop",
                        List.of("dell", "ultrabook", "developer"),
                        new BigDecimal("31990000"),
                        true,
                        now.minusSeconds(700L)
                ),
                new ProductDocument(
                        "P-1007",
                        "ASUS ROG Zephyrus G14",
                        "Laptop hieu nang cao, manh cho dev can chay local stack nang.",
                        "laptop",
                        List.of("asus", "gaming", "ryzen"),
                        new BigDecimal("38990000"),
                        true,
                        now.minusSeconds(600L)
                ),
                new ProductDocument(
                        "P-1008",
                        "Amazon Kindle Paperwhite",
                        "May doc sach gon nhe, pin lau, tot cho hoc tap va doc tai lieu.",
                        "tablet",
                        List.of("kindle", "ebook", "reading"),
                        new BigDecimal("3990000"),
                        true,
                        now.minusSeconds(500L)
                )
        );
    }
}
