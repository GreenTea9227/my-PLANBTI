package com.example.demo.boundedContext.order.service;

import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import com.example.demo.boundedContext.order.dto.OrderExchangeDto;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.entity.OrderItemStatus;
import com.example.demo.boundedContext.order.repository.OrderDetailRepository;
import com.example.demo.boundedContext.order.repository.OrderRepository;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
@ActiveProfiles("test")
class OrderDetailServiceTest {

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProductRepository productRepository;


    @DisplayName("exchange test")
    @Test
    void t1() {
        //given
        Member member = Member.builder().build();
        memberRepository.save(member);

        Product product = productRepository.save(Product.builder().count(100)
                .name("product")
                .price(1000)
                .build());

        Order order = orderRepository.save(Order.builder()
                .member(member)
                .build());

        OrderDetail orderDetail1 = OrderDetail.builder().count(2).build();
        orderDetail1.addOrder(order, product);
        orderDetailRepository.save(orderDetail1);

        OrderExchangeDto dto = new OrderExchangeDto(orderDetail1.getId(), null, 2, product.getPrice());

        //when
        orderDetailService.exchange(dto,member.getId());

        //then
        assertThat(orderDetail1.getStatus()).isEqualTo(OrderItemStatus.EXCHANGE);
    }

    @DisplayName("exchange test")
    @Test
    void t2() {
        //given
        Member member = Member.builder().build();
        memberRepository.save(member);

        Product product = productRepository.save(Product.builder().count(100)
                .name("product")
                .price(1000)
                .build());

        Order order = orderRepository.save(Order.builder()
                .member(member)
                .build());

        OrderDetail orderDetail1 = OrderDetail.builder().count(2).build();
        orderDetail1.addOrder(order, product);
        orderDetailRepository.save(orderDetail1);

        Long orderCount = order.getItemCount();
        Long totalPrice = order.getTotalPrice();
        int count = orderDetail1.getCount();

        OrderExchangeDto dto = new OrderExchangeDto(orderDetail1.getId(), product.getName(), 2, product.getPrice());

        //when
<<<<<<< HEAD
        orderDetailService.returnProduct(order.getId(), dto, member.getId());
=======
        orderDetailService.returnProduct(order.getId(),dto,member.getId());
>>>>>>> ef1c41b (feat: 관리자 교환/반품 로직 구현 완료)

        //then
        assertThat(orderDetail1.getStatus()).isEqualTo(OrderItemStatus.RETURN);
        assertThat(orderDetail1.getCount()).isEqualTo(count - dto.getCount());
        assertThat(product.getCount()).isEqualTo(100+dto.getCount());
        assertThat(order.getItemCount()).isEqualTo(orderCount - dto.getCount());
        assertThat(order.getTotalPrice()).isEqualTo(totalPrice - dto.getTotalPrice());

    }

}