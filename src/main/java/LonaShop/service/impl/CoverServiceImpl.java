package LonaShop.service.impl;

import LonaShop.model.Cover;
import LonaShop.repository.CoverRepository;
import LonaShop.service.CoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoverServiceImpl implements CoverService {

    @Autowired
    private CoverRepository coverRepository;

    @Override
    public void save(Cover cover) {
        coverRepository.save(cover);
    }

    @Override
    public Cover findById(Long id) {
        return coverRepository.findById(id).get();
    }

    @Override
    public void deleteById(Long id) {
        coverRepository.deleteById(id);
    }

    @Override
    public List<Cover> findAll() {
        return coverRepository.findAll();
    }
}
