package io.security.basicsecurity.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.security.basicsecurity.domain.entity.Resources;
import io.security.basicsecurity.repository.ResourcesRepository;
import io.security.basicsecurity.service.ResourcesService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResourcesServiceImpl implements ResourcesService {

	@Autowired
    private ResourcesRepository ResourcesRepository;

    @Transactional
    public Resources getResources(long id) {
        return ResourcesRepository.findById(id).orElse(new Resources());
    }

    @Transactional
    public List<Resources> getResources() {
        return ResourcesRepository.findAll(Sort.by(Sort.Order.asc("orderNum")));
    }

    @Transactional
    public void createResources(Resources resources){
        ResourcesRepository.save(resources);
    }

    @Transactional
    public void deleteResources(long id) {
        ResourcesRepository.deleteById(id);
    }
}
