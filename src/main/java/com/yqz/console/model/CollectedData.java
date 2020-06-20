package com.yqz.console.model;

public   class CollectedData {

        private String source;

        private String type;

        private String data;
        /**
         * 服务器时间戳，以服务器收到数据的时间为准
         */
        private long serverTs;


        public String getSource() {
                return source;
        }

        public void setSource(String source) {
                this.source = source;
        }

        public String getType() {
                return type;
        }

        public void setType(String type) {
                this.type = type;
        }

        public String getData() {
                return data;
        }

        public void setData(String data) {
                this.data = data;
        }

        public long getServerTs() {
                return serverTs;
        }

        public void setServerTs(long serverTs) {
                this.serverTs = serverTs;
        }
}