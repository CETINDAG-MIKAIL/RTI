package be.hepl.request;

import java.io.Serializable;
import java.security.cert.X509Certificate;

public class RequestSecure implements Request {
    private Request request;
    private byte[] requestEncrypted;
    private byte[] digest;
    private X509Certificate certificate;
    private byte[] signature;
    private byte[] IV;

    public RequestSecure() {}

    public RequestSecure (Request request, byte[] requestEncrypted, byte[] digest, X509Certificate certificate, byte[] signature, byte[] IV) {
        this.request = request;
        this.requestEncrypted = requestEncrypted;
        this.digest = digest;
        this.certificate = certificate;
        this.signature = signature;
        this.IV = IV;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public byte[] getRequestEncrypted() {
        return requestEncrypted;
    }

    public void setRequestEncrypted(byte[] requestEncrypted) {
        this.requestEncrypted = requestEncrypted;
    }

    public byte[] getDigest() {
        return digest;
    }

    public void setDigest(byte[] digest) {
        this.digest = digest;
    }

    public X509Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(X509Certificate certificate) {
        this.certificate = certificate;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public byte[] getIV() {
        return IV;
    }

    public void setIV(byte[] IV) {
        this.IV = IV;
    }
}
