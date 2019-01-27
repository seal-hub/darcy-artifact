package md.utm.fcim.dto;

import java.io.Serializable;

public class DependencyNode implements Serializable {

    private static final long serialVersionUID = -2649593091586008751L;

    private Long id;
    private String host;
    private Integer port;

    public DependencyNode() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "DependencyNode{" +
                "id=" + id +
                ", host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
