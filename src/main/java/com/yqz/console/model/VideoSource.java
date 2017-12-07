package com.yqz.console.model;

import java.util.Date;

public class VideoSource {
    private Long id;

    private Byte siteType;

    private String vid;

    private String title;

    private String category;

    private String thumb;

    private String tags;

    private String logo;
    private String navBar;

    private Byte state;
    private String athmMp4Url;
    private String athmM3u8Url;
    private String webUrl;
    private String publisherId;
    private String publisherName;
    private Date publisherDate;
    private Integer countPv;
    private Integer countReply;
    private Integer countLike;
    private Integer vSize;
    private Integer vWidth;
    private Integer vHeight;
    private Byte segsCount;
    private Integer vMilliseconds;
    private Integer aMilliseconds;
    private Date createTime;
    private Date updateTime;

    private Date timestamp;
    private String clientId;

    private Long collectorId;

    private String collectorName;

    public Long getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(Long collectorId) {
        this.collectorId = collectorId;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollector_name(String collector_name) {
        this.collectorName = collector_name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getSiteType() {
        return siteType;
    }

    public void setSiteType(Byte siteType) {
        this.siteType = siteType;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid == null ? "" : vid.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? "" : category.trim();
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb == null ? "" : thumb.trim();
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags == null ? "" : tags.trim();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo == null ? "" : logo.trim();
    }

    public String getNavBar() {
        return navBar;
    }

    public void setNavBar(String navBar) {
        this.navBar = navBar == null ? "" : navBar.trim();
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getAthmMp4Url() {
        return athmMp4Url;
    }

    public void setAthmMp4Url(String athmMp4Url) {
        this.athmMp4Url = athmMp4Url == null ? "" : athmMp4Url.trim();
    }

    public String getAthmM3u8Url() {
        return athmM3u8Url;
    }

    public void setAthmM3u8Url(String athmM3u8Url) {
        this.athmM3u8Url = athmM3u8Url == null ? "" : athmM3u8Url.trim();
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl == null ? "" : webUrl.trim();
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId == null ? "" : publisherId.trim();
    }

    public String getPublisherName() {
        return publisherName == null ? "" : publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName == null ? "" : publisherName.trim();
    }

    public Date getPublisherDate() {
        return publisherDate;
    }

    public void setPublisherDate(Date publisherDate) {
        this.publisherDate = publisherDate;
    }

    public Integer getCountPv() {
        return countPv == null ? 0 : countPv;
    }

    public void setCountPv(Integer countPv) {
        this.countPv = countPv;
    }

    public Integer getCountReply() {
        return countReply == null ? 0 : countReply;
    }

    public void setCountReply(Integer countReply) {
        this.countReply = countReply;
    }

    public Integer getCountLike() {
        return countLike == null ? 0 : countLike;
    }

    public void setCountLike(Integer countLike) {
        this.countLike = countLike;
    }

    public Integer getvSize() {
        return vSize == null ? 0 : vSize;
    }

    public void setvSize(Integer vSize) {
        this.vSize = vSize;
    }

    public Integer getvWidth() {
        return vWidth == null ? 0 : vWidth;
    }

    public void setvWidth(Integer vWidth) {
        this.vWidth = vWidth;
    }

    public Integer getvHeight() {
        return vHeight == null ? 0 : vHeight;
    }

    public void setvHeight(Integer vHeight) {
        this.vHeight = vHeight;
    }

    public Byte getSegsCount() {
        return segsCount == null ? 0 : segsCount;
    }

    public void setSegsCount(Byte segsCount) {
        this.segsCount = segsCount;
    }

    public Integer getvMilliseconds() {
        return vMilliseconds == null ? 0 : vMilliseconds;
    }

    public void setvMilliseconds(Integer vMilliseconds) {
        this.vMilliseconds = vMilliseconds;
    }

    public Integer getaMilliseconds() {
        return aMilliseconds == null ? 0 : aMilliseconds;
    }

    public void setaMilliseconds(Integer aMilliseconds) {
        this.aMilliseconds = aMilliseconds;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId == null ? "" : clientId.trim();
    }
}