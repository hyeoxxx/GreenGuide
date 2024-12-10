package kr.ac.baekseok.recyclehelper.Community;

public class Post {
    private String postId;        // 게시물 ID
    private String authorId;      // 작성자 ID
    private String authorName;    // 작성자 닉네임
    private String title;         // 게시물 제목
    private String content;       // 게시물 내용
    private long timestamp;       // 작성 시간
    private String category;      // 카테고리

    public Post() {}

    public Post(String postId, String authorId, String authorName, String title, String content, long timestamp, String category) {
        this.postId = postId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.category = category;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
