package com.example.demo.base.initdata;

import com.example.demo.base.Role;
<<<<<<< HEAD
import com.example.demo.boundedContext.member.entity.Address;
=======
import com.example.demo.boundedContext.category.entity.Category;
import com.example.demo.boundedContext.category.repository.CategoryRepository;
>>>>>>> 8de36f0 (feat : category, product initdata 추가)
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.AddressRepository;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.repository.OrderDetailRepository;
import com.example.demo.boundedContext.order.repository.OrderRepository;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


@Profile({"dev", "test"})
@Configuration
public class CustomInitData {
    @Bean
    CommandLineRunner initData(MemberRepository memberRepository, PasswordEncoder passwordEncoder,
                               OrderRepository orderRepository, ProductRepository productRepository,
                               OrderDetailRepository orderDetailRepository, AddressRepository addressRepository,CategoryRepository categoryRepository ) {


        return new CommandLineRunner() {

            @Override
            @Transactional
            public void run(String... args) throws Exception {
                Product product1 = productRepository.save(Product.builder()
                                .count(10)
                        .price(15000)
                        .name("product1").build());


                String encode = passwordEncoder.encode("1111");
                Member user = Member.builder()
                        .username("user1")
                        .password(encode)
                        .phoneNumber("010-1111-1111")
                        .email("user1@naver.com")
                        .build();
                user.addRole(Role.USER);
                memberRepository.save(user);

                Order order = Order.builder()
                        .orderName("orderName")
                        .member(user)
                        .build();
                orderRepository.save(order);

                OrderDetail orderDetail1 = OrderDetail.builder()
                        .product(product1)
                        .count(2)
                        .build();
                orderDetail1.addOrder(order,product1);
                orderDetailRepository.save(orderDetail1);

                OrderDetail orderDetail2 = OrderDetail.builder()
                        .product(product1)
                        .count(2)
                        .build();
                orderDetail2.addOrder(order,product1);
                orderDetailRepository.save(orderDetail2);

                Address address = Address.builder()
                        .member(user)
                        .name("배송지 1")
                        .addr("서울시")
                        .addrDetail("중구")
                        .zipCode("00000")
                        .phoneNumber("01012345678")
                        .build();
                addressRepository.save(address);

                Member admin = Member.builder()
                        .username("admin")
                        .password(encode)
                        .phoneNumber("010-1111-1111")
                        .email("user1@naver.com")
                        .build();
                admin.addRole(Role.USER);
                admin.addRole(Role.ADMIN);
                memberRepository.save(admin);

                Category istj = categoryRepository.save(
                        Category.builder()
                                .id(1L)
                                .name("istj")
                                .build()
                );

                Product save = productRepository.save(
                        Product.builder()
                                .category(istj)
                                .name("뱅갈고무나무")
                                .price(39000)
                                .count(3)
                                .salePrice(25000)
                                .build()
                );


            }
        };
    }
}
