package LonaShop.service;

import LonaShop.model.Cover;

import java.util.List;

public interface CoverService extends CommonService<Cover> {
    List<Cover> getMainCoverList();

    List<Cover> getSubCoverList();

}
