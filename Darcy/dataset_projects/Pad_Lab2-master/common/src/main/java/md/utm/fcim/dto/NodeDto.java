package md.utm.fcim.dto;

import java.io.Serializable;
import java.util.List;

public class NodeDto implements Serializable {

    private static final long serialVersionUID = -575076196921111424L;

    private Long id;
    private String host;
    private Integer port;
    private List<DependencyNode> dependencies;
    private List<UserDto> users;

    public NodeDto() {
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

    public List<DependencyNode> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<DependencyNode> dependencies) {
        this.dependencies = dependencies;
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "NodeDto{" +
                ", id='" + id + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", dependencies=" + dependencies +
                ", users=" + users +
                '}';
    }
}
