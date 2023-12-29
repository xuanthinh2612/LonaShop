//package LonaShop.controller.admin;
//
//import LonaShop.common.CommonConst;
//import LonaShop.model.Cover;
//import LonaShop.model.Image;
//import LonaShop.service.CoverService;
//import LonaShop.service.file.FilesStorageService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.util.ObjectUtils;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//@Controller
//@RequestMapping("/admin/cover")
//public class CoverController extends AdminBaseController {
//
//    @Autowired
//    private Environment environment;
//
//    @Autowired
//    private FilesStorageService filesStorageService;
//
//    @Autowired
//    CoverService coverService;
//
//    @GetMapping("/list")
//    public String getAllCover(Model model) {
//        List<Cover> coverList = coverService.findAll();
//        model.addAttribute("coverList", coverList);
//        return "admin/cover/index";
//    }
//
//    @GetMapping("/new")
//    public String initNewCover(Model model) {
//        Cover cover = new Cover();
//        model.addAttribute("cover", cover);
//        return "admin/cover/new";
//    }
//
//    @PostMapping("/create")
//    public String createCover(@ModelAttribute("cover") Cover cover, BindingResult result, MultipartFile[] imagesFiles, Model model) {
//
//        if (result.hasErrors() || ObjectUtils.isEmpty(imagesFiles)) {
//            return "admin/cover/new";
//        }
//
//        List<Image> imageListCover = new ArrayList<>();
//
//        for (MultipartFile file : imagesFiles) {
//            Image image = new Image();
//            Long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
//            String fileName = "cover-" + timestamp + "-" + file.getOriginalFilename();
//            try {
//                filesStorageService.save(file, fileName);
//                image.setImageName(fileName);
//                image.setImageUrl("/" + environment.getProperty("upload.path") + "/" + fileName);
//                imageListCover.add(image);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        cover.setImages(imageListCover);
//        coverService.save(cover);
//        return "redirect:/admin/cover/list";
//    }
//
//    @PostMapping("/delete/{id}")
//    public String deleteCover(@PathVariable("id") Long id, Model model) {
//
//        List<Cover> coverList = coverService.findAll();
//        if (coverList.size() <= 1) {
//            return "redirect:/admin/cover/list";
//        }
//
//        boolean isOnCover = false;
//        for (Cover cover : coverList) {
//            if (cover.getStatus() == CommonConst.FLAG_ON && !Objects.equals(cover.getId(), id)) {
//                isOnCover = true;
//                break;
//            }
//        }
//
//        if (isOnCover) {
//            coverService.deleteById(id);
//            return "redirect:/admin/cover/list";
//        } else {
//            return "redirect:/admin/cover/list";
//        }
//    }
//
//    @PostMapping("/disable/{id}")
//    public String disable(@PathVariable("id") Long id, Model model) {
//
//        List<Cover> coverList = coverService.findAll();
//
//        boolean isOnCover = false;
//
//        for (Cover cover : coverList) {
//            if (cover.getStatus() == CommonConst.FLAG_ON && !Objects.equals(cover.getId(), id)) {
//                isOnCover = true;
//                break;
//            }
//        }
//        if (isOnCover) {
//            Cover cover = coverService.findById(id);
//            cover.setStatus(CommonConst.FLAG_OFF);
//            coverService.save(cover);
//        } else {
//            return "redirect:/admin/cover/list";
//        }
//        return "redirect:/admin/cover/list";
//    }
//
//    @PostMapping("/enable/{id}")
//    public String enable(@PathVariable("id") Long id, Model model) {
//        Cover cover = coverService.findById(id);
//        cover.setStatus(CommonConst.FLAG_ON);
//        coverService.save(cover);
//        return "redirect:/admin/cover/list";
//    }
//
//
//}
