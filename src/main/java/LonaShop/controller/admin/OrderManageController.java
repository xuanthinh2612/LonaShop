package LonaShop.controller.admin;

import LonaShop.common.CommonConst;
import LonaShop.model.UserOrder;
import LonaShop.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/order")
public class OrderManageController {

    @Autowired
    private UserOrderService orderService;

    @GetMapping("/list")
    private String listOrder(@RequestParam("status") int status, Model model) {

        if (ObjectUtils.isEmpty(status)) {
            status = CommonConst.ORDERED;
        }

        List<UserOrder> orderList = orderService.findAllByStatus(status);
        model.addAttribute("orderList", orderList);
        model.addAttribute(CommonConst.ORDER_STATUS, status);
        return "admin/order/index";
    }

    @PostMapping("/updateStatus/{id}")
    private String updateStatus(@PathVariable("id") Long id, @RequestParam("status") int status, Model model) {
        UserOrder order = orderService.findById(id);
        switch (status) {
            case CommonConst.CONFIRMED:
                order.setStatus(CommonConst.CONFIRMED);
                break;
            case CommonConst.PACKING:
                order.setStatus(CommonConst.PACKING);
                break;
            case CommonConst.DELIVERED:
                order.setStatus(CommonConst.DELIVERED);
                break;
            case CommonConst.DENIED:
                order.setStatus(CommonConst.DENIED);
                break;
        }

        order.setUpdatedAt(new Date());
        orderService.save(order);
        return listOrder(status, model);
    }

    @PostMapping("/delete/{id}")
    private String deleteOrder(@PathVariable("id") Long id, Model model) {
        orderService.deleteById(id);
        return listOrder(CommonConst.ORDERED, model);
    }
}
