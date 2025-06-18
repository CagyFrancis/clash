package cn.njupt.xjy.analyzers;

import java.util.Objects;

public class Context {
    private String identity;
    private String callsite;

    public Context(String identity, String callsite) {
        this.identity = identity;
        this.callsite = callsite;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.identity, this.callsite);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Context other = (Context) obj;
        return Objects.equals(this.identity, other.identity) && Objects.equals(this.callsite, other.callsite);
    }

    public String getIdentity() {
        return this.identity;
    }

    public String getCallSite() {
        return this.callsite;
    }
}