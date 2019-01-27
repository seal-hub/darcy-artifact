package md.utm.fcim.dto;

import md.utm.fcim.constant.FieldName;
import md.utm.fcim.constant.MethodName;
import md.utm.fcim.constant.NodeType;
import md.utm.fcim.constant.OperationType;

import java.io.Serializable;

public class MessageDto implements Serializable {

    private static final long serialVersionUID = -8524201840600026558L;

    private MethodName method;

    private FieldName field;

    private OperationType operation;

    private String value;

    private NodeType nodeType;

    public MessageDto(MethodName method, FieldName field, OperationType operation, String value) {
        this.method = method;
        this.field = field;
        this.operation = operation;
        this.value = value;
    }

    public MessageDto(MethodName method, FieldName field, OperationType operation) {
        this.method = method;
        this.field = field;
        this.operation = operation;
    }

    public MessageDto(MethodName method) {
        this.method = method;
    }

    public MethodName getMethod() {
        return method;
    }

    public void setMethod(MethodName method) {
        this.method = method;
    }

    public FieldName getField() {
        return field;
    }

    public void setField(FieldName field) {
        this.field = field;
    }

    public OperationType getOperation() {
        return operation;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "method=" + method +
                ", field=" + field +
                ", operation=" + operation +
                ", value='" + value + '\'' +
                '}';
    }
}
