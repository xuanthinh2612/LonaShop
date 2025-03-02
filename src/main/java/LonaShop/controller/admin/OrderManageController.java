package LonaShop.controller.admin;

import LonaShop.common.CommonConst;
import LonaShop.model.UserOrder;
import LonaShop.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/order")
public class OrderManageController extends AdminBaseController {

    @Autowired
    private UserOrderService orderService;
    @Autowired
    private CommonConst commonConst;

    @ModelAttribute("CommonConst")
    private CommonConst returnCommonConst() {
        return commonConst;
    }

    @GetMapping(value = "list", params = "waitForAdminConfirmList")
    private String waitForAdminConfirmList(Model model) {

        List<UserOrder> orderList = orderService.findWaitForConfirmList();
        model.addAttribute("orderList", orderList);
        model.addAttribute(CommonConst.ORDER_STATUS, CommonConst.ORDERED_STATUS);
        model.addAttribute("paymentStatus", CommonConst.USER_CONFIRMED_PAY);

        return "admin/order/index";
    }

    @GetMapping(value = "list", params = "confirmedOrderList")
    private String confirmedOrderList(Model model) {

        List<UserOrder> orderList = orderService.findAllByStatus(CommonConst.ADMIN_CONFIRMED_STATUS);
        model.addAttribute("orderList", orderList);
        model.addAttribute(CommonConst.ORDER_STATUS, CommonConst.ADMIN_CONFIRMED_STATUS);
        return "admin/order/index";
    }

    @GetMapping(value = "list", params = "waitForPackageList")
    private String waitForPackageList(Model model) {

        List<UserOrder> orderList = orderService.findAllByStatus(CommonConst.PACKING_STATUS);
        model.addAttribute("orderList", orderList);
        model.addAttribute(CommonConst.ORDER_STATUS, CommonConst.PACKING_STATUS);
        return "admin/order/index";
    }

    @GetMapping(value = "list", params = "deliveredList")
    private String deliveredList(Model model) {

        List<UserOrder> orderList = orderService.findAllByStatus(CommonConst.DELIVERED_STATUS);
        model.addAttribute("orderList", orderList);
        model.addAttribute(CommonConst.ORDER_STATUS, CommonConst.DELIVERED_STATUS);
        return "admin/order/index";
    }

    @GetMapping(value = "list", params = "deniedOrderList")
    private String deniedOrderList(Model model) {

        List<UserOrder> orderList = orderService.findAllByStatus(CommonConst.DENIED_STATUS);
        model.addAttribute("orderList", orderList);
        model.addAttribute(CommonConst.ORDER_STATUS, CommonConst.DENIED_STATUS);
        return "admin/order/index";
    }

    @GetMapping(value = "list", params = "succeedOrderList")
    private String succeedOrderList(Model model) {

        List<UserOrder> orderList = orderService.findAllByStatus(CommonConst.SUCCESS_STATUS);
        model.addAttribute("orderList", orderList);
        model.addAttribute(CommonConst.ORDER_STATUS, CommonConst.SUCCESS_STATUS);
        return "admin/order/index";
    }

    @GetMapping(value = "list", params = "waitingForPayOrderList")
    private String waitingForPayOrderList(Model model) {

        List<UserOrder> orderList = orderService.findAllByStatusAndPaymentStatus(CommonConst.ORDERED_STATUS, CommonConst.WAITING_FOR_PAY);
        model.addAttribute("orderList", orderList);
        model.addAttribute(CommonConst.ORDER_STATUS, CommonConst.ORDERED_STATUS);
        model.addAttribute("paymentStatus", CommonConst.WAITING_FOR_PAY);
        return "admin/order/index";
    }

    @GetMapping(value = "list", params = "returnList")
    private String returnList(Model model) {

        List<UserOrder> orderList = orderService.findAllByStatus(CommonConst.RETURN_STATUS);
        model.addAttribute("orderList", orderList);
        model.addAttribute(CommonConst.ORDER_STATUS, CommonConst.RETURN_STATUS);
        return "admin/order/index";
    }

    @GetMapping(value = "list", params = "userCanceledList")
    private String userCanceledList(Model model) {

        List<UserOrder> orderList = orderService.findAllByStatus(CommonConst.USER_CANCELED_STATUS);
        model.addAttribute("orderList", orderList);
        model.addAttribute(CommonConst.ORDER_STATUS, CommonConst.USER_CANCELED_STATUS);
        return "admin/order/index";
    }

    @PostMapping("/updateStatus/{id}")
    private String updateStatus(@PathVariable("id") Long id, @RequestParam("status") int status, Model model) {
        UserOrder order = orderService.findById(id);

        order.setStatus(status);
        if (status == CommonConst.ADMIN_CONFIRMED_STATUS) {
            order.setPaymentStatus(CommonConst.ADMIN_CONFIRMED_PAID);
        }
        order.setUpdatedAt(new Date());
        orderService.save(order);

        return switch (status) {
            case CommonConst.ADMIN_CONFIRMED_STATUS -> "redirect:/admin/order/list?confirmedOrderList";
            case CommonConst.PACKING_STATUS -> "redirect:/admin/order/list?waitForPackageList";
            case CommonConst.DELIVERED_STATUS -> "redirect:/admin/order/list?deliveredList";
            case CommonConst.DENIED_STATUS -> "redirect:/admin/order/list?deniedOrderList";
            case CommonConst.SUCCESS_STATUS -> "redirect:/admin/order/list?succeedOrderList";
            case CommonConst.RETURN_STATUS -> "redirect:/admin/order/list?returnList";
            default -> "redirect:/admin/quan-ly";
        };
    }

    @PostMapping("/delete/{id}")
    private String deleteOrder(@PathVariable("id") Long id, Model model) {
        orderService.deleteById(id);
        return "redirect:/admin/order/list?waitForAdminConfirmList";
    }
}
