package com.gaokao.common.meta.bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class H5SceneInfo {
    @JSONField(name = "h5_info")
    private H5 h5Info;

    public static class H5 {
        private String type;
        @JSONField(name = "wap_url")
        private String wapUrl;
        @JSONField(name = "wap_name")
        private String wapName;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getWapUrl() {
            return wapUrl;
        }

        public void setWapUrl(String wapUrl) {
            this.wapUrl = wapUrl;
        }

        public String getWapName() {
            return wapName;
        }

        public void setWapName(String wapName) {
            this.wapName = wapName;
        }
    }

    public H5 getH5Info() {
        return h5Info;
    }

    public void setH5Info(H5 h5Info) {
        this.h5Info = h5Info;
    }
}
