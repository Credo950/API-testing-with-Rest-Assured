public class PostDTO {
    private int userId;
    private int id;
    private String title;
    private String body;

    public int getUserId() {
        return userId;
    }

    public PostDTO setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getId() {
        return id;
    }

    public PostDTO setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PostDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public PostDTO setBody(String body) {
        this.body = body;
        return this;
    }
}
