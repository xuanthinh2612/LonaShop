package LonaShop.service.impl;

import LonaShop.model.SubContent;
import LonaShop.repository.SubContentRepository;
import LonaShop.service.SubContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubContentServiceImpl implements SubContentService {

    @Autowired
    private SubContentRepository subContentRepository;

    @Override
    public void save(SubContent subContent) {
        subContentRepository.save(subContent);
    }

    @Override
    public SubContent findById(Long id) {
        return subContentRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        subContentRepository.deleteById(id);
    }

    @Override
    public List<SubContent> findAll() {
        return subContentRepository.findAll();
    }
}
