package com.example.demo.boundedContext.member.service;

import com.example.demo.base.event.EventAfterCreateAddress;
import com.example.demo.base.exception.DataNotFoundException;
import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.AddressRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final ApplicationEventPublisher publisher;

    public Address findById(Long id) {
        Optional<Address> address = addressRepository.findById(id);
        if (address.isEmpty()) {
            throw new DataNotFoundException("존재하지 않는 주소입니다.");
        }
        return address.get();
    }

    public Address findByIdAndDeleteDateIsNull(Long id) {
        Optional<Address> address = addressRepository.findByIdAndDeleteDateIsNull(id);
        if (address.isEmpty()) {
            throw new DataNotFoundException("존재하지 않는 주소입니다.");
        }
        return address.get();
    }

    public Address create(Member member, String name, String addr, String addrDetail, String zipCode, String phoneNumber, boolean isDefault) {
        Address address = Address.builder()
                .name(name)
                .addr(addr)
                .addrDetail(addrDetail)
                .zipCode(zipCode)
                .phoneNumber(phoneNumber)
                .isDefault(isDefault)
                .build();
        addressRepository.save(address);

        publisher.publishEvent(new EventAfterCreateAddress(this, member, address));

        return address;
    }

    public Address modify(Long id, String name, String addr, String addrDetail, String zipCode, String phoneNumber, boolean isDefault) {
        Address address = findByIdAndDeleteDateIsNull(id);
        Address address1 = address.toBuilder()
                .name(name)
                .addr(addr)
                .addrDetail(addrDetail)
                .zipCode(zipCode)
                .phoneNumber(phoneNumber)
                .isDefault(isDefault)
                .build();

        if (address.equals(address1)) throw new ValidationException("already exists");

        addressRepository.save(address1);
        return address1;
    }

    // soft-delete
    public void delete(Address address) {
        Address address1 = address.toBuilder()
                .deleteDate(LocalDateTime.now())
                .build();
        addressRepository.save(address1);
    }
}