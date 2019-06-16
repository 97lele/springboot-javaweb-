package com.gdut.xg.shop.VO;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class HistroyVO {
private String name;
private String lastViewDate;
private String id;
private String imgurl;
private String price;
private String url;

}
