package LonaShop.service;

import LonaShop.model.Inquiry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InquiryService extends CommonService<Inquiry> {

    List<Inquiry> findAllByStatus(int status);
}
