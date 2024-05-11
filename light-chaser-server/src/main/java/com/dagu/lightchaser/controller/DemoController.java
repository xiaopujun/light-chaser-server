package com.dagu.lightchaser.controller;

import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test/")
public class DemoController {

    @PostMapping("/testNoType")
    public Object testNoType(@RequestBody Map<String, Object> params) {
        //数据个数
        int dataNum = (int) params.get("dataNum");
        if (dataNum == 0)
            dataNum = 5;
        List<Map<String, Object>> maps = new ArrayList<>();
        int base = 2000;
        for (int i = 0; i < dataNum; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", base + "");
            map.put("value", ((int) (Math.random() * 501)));
            maps.add(map);
            base++;
        }
//        int a = 1 / 0;
        return maps;
    }

    @PostMapping("/testHasType")
    public Object testHasType(@RequestBody Map<String, Object> params) {
        //分类个数
        int sorts = (int) params.get("sorts");
        //数据个数
        int dataNum = (int) params.get("dataNum");
        //结果集
        List<Map<String, Object>> res = new ArrayList<>();

        //年份基数
        int base = 2000;
        for (int i = 0; i < sorts; i++) {
            String sortName = "sort" + (i + 1);
            for (int j = 0; j < dataNum; j++) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", base + "");
                map.put("value", ((int) (Math.random() * 501)));
                map.put("type", sortName);
                res.add(map);
                base++;
            }
            base = 2000;
        }

        return res;
    }

    @GetMapping("/testNumber")
    public float testNumber(@RequestParam("min") int min, @RequestParam("max") int max) {
        float number = (float) (Math.random() * (max - min) + min);
        return new Float(new DecimalFormat("#.0000").format(number));
    }

}

