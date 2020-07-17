package com.cofco.sys.controller.members;

import com.cofco.utils.PhotoUtils;
import com.cofco.utils.UploadUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("upload")
public class UploadController {

    @RequestMapping(value = "/image/{type}", method = RequestMethod.POST)
    public Map<String, Object> image(MultipartFile file, HttpServletRequest request, @PathVariable String type) {
        Map<String, Object> map = new HashMap<>();
        try {
            String path = request.getSession().getServletContext().getRealPath("\\upload\\" + type + "\\");
            String image = UploadUtil.uploadFile(file, path);
            byte[] bytes = PhotoUtils.getImageBinary(path + "\\" + image, "jpg");
            map.put("code", 0);
            map.put("image", bytes);
        } catch (Exception e) {
            map.put("code", 1);
            e.printStackTrace();
        }
        return map;
    }
}
