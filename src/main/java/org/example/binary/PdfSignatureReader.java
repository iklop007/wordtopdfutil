package org.example.binary;

import org.bouncycastle.cms.CMSException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;

public class PdfSignatureReader {
    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

//        try (FileInputStream fis = new FileInputStream("example.pdf")) {
//            CMSSignedData signedData = new CMSSignedData(fis);
//            SignerInformationStore signers = signedData.getSignerInfos();
//            Collection<SignerInformation> signersCollection = signers.getSigners();
//
//            for (SignerInformation signer : signersCollection) {
//                X509Certificate cert = signer.getSID().getIssuer().getCertificate();
//                System.out.println("Signer: " + cert.getSubjectDN());
//                System.out.println("Signing time: " + signer.getSigningTime());
//                System.out.println("Signature algorithm: " + signer.getDigestAlgorithmID().getAlgorithm());
//            }
//        } catch (CMSException | FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

}
