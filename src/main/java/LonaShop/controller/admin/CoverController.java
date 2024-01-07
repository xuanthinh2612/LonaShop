package LonaShop.controller.admin;

import LonaShop.common.CommonConst;
import LonaShop.controller.helper.Helper;
import LonaShop.model.Cover;
import LonaShop.model.Image;
import LonaShop.service.CoverService;
import LonaShop.service.file.FilesStorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin/cover")
public class CoverController extends AdminBaseController {

    @Autowired
    private Environment environment;

    @Autowired
    private FilesStorageService filesStorageService;

    @Autowired
    private Helper helper;

    @Autowired
    CoverService coverService;

    @GetMapping("/list")
    public String getAllCover(Model model) {
        List<Cover> coverList = coverService.findAll();

        int totalMainCoverNum = coverService.getMainCoverList().size();
        int totalSubCoverNum = coverService.getSubCoverList().size();

        model.addAttribute("coverList", coverList);
        model.addAttribute("totalMainCoverNum", totalMainCoverNum);
        model.addAttribute("totalSubCoverNum", totalSubCoverNum);
        return "admin/cover/index";
    }

    @GetMapping("/new")
    public String initNewCover(Model model) {
        Cover cover = new Cover();
        model.addAttribute("cover", cover);
        return "admin/cover/new";
    }

    @PostMapping(params = "cancel")
    public String cancelNewCover(Model model) {
        return "redirect:/admin/cover/list";
    }

    @PostMapping(params = "create")
    public String createCover(@Valid @ModelAttribute("cover") Cover cover, BindingResult result, @RequestParam("imageFile") MultipartFile imageFile, Model model) {

        if (result.hasErrors()) {
            return "admin/cover/new";
        }

        if (imageFile.isEmpty()) {
            result.addError(new FieldError("cover", "image", "Mục này không thể bỏ trống"));
            return "admin/cover/new";
        }

        Image image = new Image();
        String fileName = helper.genRandomFileName(imageFile.getOriginalFilename());
        try {
            filesStorageService.save(imageFile, fileName);
            image.setImageName(fileName);
            image.setImageUrl("/" + environment.getProperty("upload.path") + "/" + fileName);
            cover.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        coverService.save(cover);
        return "redirect:/admin/cover/list";
    }


    @PostMapping("/delete/{id}")
    public String deleteCover(@PathVariable("id") Long id, Model model) {

        List<Cover> coverList = coverService.findAll();
        if (coverList.size() <= 1) {
            return "redirect:/admin/cover/list";
        }

        boolean isOnCover = false;
        for (Cover cover : coverList) {
            if (cover.getStatus() == CommonConst.FLAG_ON && !Objects.equals(cover.getId(), id)) {
                isOnCover = true;
                break;
            }
        }
        Cover cover = coverService.findById(id);

        if (!ObjectUtils.isEmpty(cover) && isOnCover) {
            Image image = cover.getImage();
            filesStorageService.deleteByFileName(image.getImageName());
            coverService.deleteById(id);
        }
        return "redirect:/admin/cover/list";
    }

    @PostMapping("/disable/{id}")
    public String disable(@PathVariable("id") Long id, Model model) {
        Cover cover = coverService.findById(id);
        cover.setStatus(CommonConst.DISABLED);
        coverService.save(cover);
        return "redirect:/admin/cover/list";
    }

    @PostMapping("/setMainCover/{id}")
    public String setMainCover(@PathVariable("id") Long id, Model model) {
        Cover cover = coverService.findById(id);
        cover.setStatus(CommonConst.MAIN_COVER);
        coverService.save(cover);
        return "redirect:/admin/cover/list";
    }

    @PostMapping("/setSubCover/{id}")
    public String setSubCover(@PathVariable("id") Long id, Model model) {
        Cover cover = coverService.findById(id);
        cover.setStatus(CommonConst.SUB_COVER);
        coverService.save(cover);
        return "redirect:/admin/cover/list";
    }

    //========================= PRIVATE METHOD =====================
//    private void deleteImageFile(Image image) {
//        try {
//            // delete image file
//            filesStorageService.deleteByFileName(image.getImageName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
