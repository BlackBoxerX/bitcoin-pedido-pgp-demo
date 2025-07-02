package bitcoin_pedido.meuprojeto.util;

import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;
import org.bouncycastle.bcpg.ArmoredOutputStream;

import java.io.*;
import java.util.Iterator;

public class PgpUtil {

    public static String encryptWithPGP(String publicKeyArmored, String texto) throws Exception {
        PGPPublicKey pubKey = readPublicKey(publicKeyArmored);

        ByteArrayOutputStream encOut = new ByteArrayOutputStream();
        ArmoredOutputStream armoredOut = new ArmoredOutputStream(encOut);

        ByteArrayOutputStream literalOut = new ByteArrayOutputStream();
        PGPUtil.writeFileToLiteralData(literalOut, PGPLiteralData.BINARY, new ByteArrayInputStream(texto.getBytes()), new File("mensagem.txt"));

        PGPEncryptedDataGenerator encryptor = new PGPEncryptedDataGenerator(
                new JcaPGPDataEncryptorBuilder(PGPEncryptedData.CAST5)
                        .setWithIntegrityPacket(true)
                        .setSecureRandom(new java.security.SecureRandom())
                        .setProvider("BC")
        );
        encryptor.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(pubKey).setProvider("BC"));

        OutputStream cOut = encryptor.open(armoredOut, literalOut.toByteArray().length);
        cOut.write(literalOut.toByteArray());
        cOut.close();
        armoredOut.close();

        return encOut.toString();
    }

    private static PGPPublicKey readPublicKey(String publicKeyArmored) throws Exception {
        InputStream keyIn = new ByteArrayInputStream(publicKeyArmored.getBytes());
        PGPPublicKeyRingCollection keyRings = new PGPPublicKeyRingCollection(
                PGPUtil.getDecoderStream(keyIn), new JcaKeyFingerprintCalculator());

        Iterator<PGPPublicKeyRing> keyRingIter = keyRings.getKeyRings();
        while (keyRingIter.hasNext()) {
            PGPPublicKeyRing keyRing = keyRingIter.next();
            Iterator<PGPPublicKey> keyIter = keyRing.getPublicKeys();
            while (keyIter.hasNext()) {
                PGPPublicKey key = keyIter.next();
                if (key.isEncryptionKey()) {
                    return key;
                }
            }
        }
        throw new IllegalArgumentException("Chave pública PGP inválida ou sem permissão para criptografia.");
    }
}



