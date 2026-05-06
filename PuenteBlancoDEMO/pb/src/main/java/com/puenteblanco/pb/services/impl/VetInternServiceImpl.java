package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.dto.response.VetInternSimpleResponseDto;
import com.puenteblanco.pb.repository.UserRepository;
import com.puenteblanco.pb.services.interfaces.VetInternService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VetInternServiceImpl implements VetInternService {

    private final UserRepository userRepository;

    public VetInternServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<VetInternSimpleResponseDto> findAllInterns() {
        return userRepository.findAllInterns();
    }
}
