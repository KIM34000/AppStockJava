package app.stock.tools.crypto;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
// la méthode String generateHash(String password) => 
// va se générer un hash qu'on peut stocket en bdd
// boolean validatePassword(String originalPassword, String storedPassword)
// va vérifier que le mot de passe renté par l'utilisateur (originalPassword)
// correspond bien au hash qu'il y a dans la bdd (storedPassword)
// lorsque on hash un mot de passe avec PBKDF2, on a besoin d'un seed, 
// et d'un nombre d'itération et ça va fournir le hash
// en bdd, on stock : "itération:seed:hash"
// lorsque l'utilisateur rentre un mot de passe,
// on doit générer le hash avec les mêmes conditions que le hash qui est dans la bdd
// donc on récupere le nombre d'itération et le seed du hash de la bdd
// on calcul le hash du mot de passe renté par l'utilisateur
// si ce hash correspond au hash qu'il y a en bdd, alors le mot de passe est identique
public class Hasher {

	private Hasher() {
		// Utility class
	}
        //  return de la méthode : un tableau de byte
	private static byte[] getSalt() {
		SecureRandom sr = new SecureRandom();
                // création du tableau de longeur 16
		byte[] salt = new byte[16];
                // rempli de manière aléatoire le tableau
                // la méthode renvoie un tableau de 16 bytes rempli de manière aléatoire
		sr.nextBytes(salt);
		return salt;
	}
        // fromHex et toHex : c'est pour transformer une String en tableau de bytes ou l'inverse
	private static byte[] fromHex(String hex) {
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}

	private static String toHex(byte[] array) {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);

		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		}
		return hex;
	}

	private static byte[] generateHash(char[] chars, byte[] salt, int iterations, final int hashLength)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, hashLength * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		return skf.generateSecret(spec).getEncoded();
	}

	public static String generateHash(String password) throws GeneralSecurityException {
		int iterations = 65536;
		char[] chars = password.toCharArray();
		byte[] salt = getSalt();

		final int hashLength = 64;
		byte[] hash = generateHash(chars, salt, iterations, hashLength);
		return iterations + ":" + toHex(salt) + ":" + toHex(hash);
	}

	public static boolean validatePassword(String originalPassword, String storedPassword)
			throws GeneralSecurityException {
		String[] parts = storedPassword.split(":");
		int iterations = Integer.parseInt(parts[0]);

		byte[] salt = fromHex(parts[1]);
		byte[] hash = fromHex(parts[2]);
		byte[] testHash = generateHash(originalPassword.toCharArray(), salt, iterations, hash.length);

		int diff = hash.length ^ testHash.length;
		for (int i = 0; i < hash.length && i < testHash.length; i++) {
			diff |= hash[i] ^ testHash[i];
		}
		return diff == 0;
	}
}
