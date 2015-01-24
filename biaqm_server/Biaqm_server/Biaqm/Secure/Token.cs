using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography.X509Certificates;
using System.Web;

namespace Biaqm.Secure
{
    public class Token
    {
        public Token() { }
        public Token(string userId, string fromIP)
        {
            UserId = userId;
            IP = fromIP;
        }

        public string UserId { get; private set; }
        public string IP { get; private set; }

        public string Encrypt()
        {
            CryptographyHelper cryptographyHelper = new CryptographyHelper();
            X509Certificate2 certificate = cryptographyHelper.GetX509Certificate("CN=BiaqmEli-Token");
            return cryptographyHelper.Encrypt(certificate, this.ToString());
        }

        public override string ToString()
        {
            return String.Format("UserId={0};IP={1}", this.UserId, this.IP);
        }

        public static Token Decrypt(string encryptedToken)
        {
            string decrypted = "";
            CryptographyHelper cryptographyHelper = new CryptographyHelper();
            if (cryptographyHelper != null)
            {
                X509Certificate2 certificate = cryptographyHelper.GetX509Certificate("CN=BiaqmEli-Token");
                if (certificate != null)
                {
                    decrypted = cryptographyHelper.Decrypt(certificate, encryptedToken);
                }
                else throw new Exception("certificate null");
            }
            else throw new Exception("cryptographyHelper null");


            //Splitting it to dictionary
            Dictionary<string, string> dictionary = decrypted.ToDictionary();
            return new Token(dictionary["UserId"], dictionary["IP"]);
        }
    }
}