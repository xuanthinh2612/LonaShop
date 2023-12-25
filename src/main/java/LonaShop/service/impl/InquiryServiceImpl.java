package LonaShop.service.impl;

import LonaShop.model.Inquiry;
import LonaShop.repository.InquiryRepository;
import LonaShop.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InquiryServiceImpl implements InquiryService {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Override
    public void save(Inquiry inquiry) {
        inquiry.setCreatedAt(new Date());
        inquiry.setUpdatedAt(new Date());
        inquiryRepository.save(inquiry);
    }

    @Override
    public Inquiry findById(Long id) {
        return inquiryRepository.findById(id).get();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<Inquiry> findAll() {
        return inquiryRepository.findAll();
    }

    @Override
    public List<Inquiry> findAllByStatus(int status) {
        return inquiryRepository.findAllByStatus(status);
    }
}
