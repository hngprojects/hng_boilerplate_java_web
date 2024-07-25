package hng_java_boilerplate.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import hng_java_boilerplate.user.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class TwoFactorAuthUtils {

    private String secretKey;

    private UserRepository userRepository;


    public String generateSecretKey() {
      GoogleAuthenticator gAuth = new GoogleAuthenticator();
      final GoogleAuthenticatorKey key = gAuth.createCredentials();
      this.secretKey = key.getKey();
    return this.secretKey;
    }


    public byte[] generateQRCode(String secret, String email) throws WriterException, IOException {
        String otpAuthURL = "otpauth://totp/HNG:" + email + "?secret=" + secret + "&issuer=HNG";
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    public boolean verifyTotpCode(String secret, int code) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.authorize(secret, code);
    }


    public String[] generateBackupCodes() {
        return new String[]{"98765432", "87654321", "76543210", "65432109", "54321098"};
    }


    public int generateTotpCode(String secret) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.getTotpPassword(secret);
    }

}
