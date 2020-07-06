import os
from flask import Flask, flash, request, redirect, render_template
from werkzeug.utils import secure_filename
from flask import Flask
import json
import pathlib #pip install pathlib
import sqlite3
import os
from Crypto.Cipher import AES
import binascii


BS = 16
pad = lambda s: s + (BS - len(s) % BS) * chr(BS - len(s) % BS)
unpad = lambda s : s[0:-ord(s[-1])]


app = Flask(__name__)
makejson = json.dumps

def decrypt(encrypted_text,key):
    print "========================"
    print "Entered decrypt"
    encr = encrypted_text
    print "encr before base64 decode = ",encr
    encr = encr.decode("base64")
    print "encr after base64 decode = ",encr
    print "key before utf-8 decode = ",key
    key = key.decode('utf-8')
    print "key after utf-8 decode = ",key
    print "========================"
    iv = 16 * '\x00'
    cipher = AES.new(key, AES.MODE_CBC, iv)
    decrypt = cipher.decrypt(encr)
    #decrypted_text = decrypt.decode('utf-8')
    decrypted_text = decrypt.decode('utf-8')
    print "########################"
    #v9B0lVlGzpWe8LQ/2K1Z/xwmoPJehqJ4Qx0wlylIimY= -> 1337133713371338
    print "decrypted_text = ",decrypted_text
    unpadded = unpad(decrypted_text)
    print "unpadded = ",unpadded
    return unpadded

@app.route('/show_data', methods=['POST'])
def show_data():
    print "entererd now"
    key = "y0u3c4ntf1ndth1skeyc0zits0s3cur3"
    req_data = request.get_json()
    print req_data

    documentID = req_data['documentId'].strip()
    print "documentID = ",documentID
    signedDocumentID = req_data['signedDocumentID']
    print "signedDocumentID = ",signedDocumentID

    decryptedDocumentID = decrypt(signedDocumentID,key)
    print "decryptedDocumentID= "+decryptedDocumentID
    #print "Comparing decryptedDocumentID="+decryptedDocumentID+" and documentID ="+documentID
    if (decryptedDocumentID.strip() == documentID.strip()):
        if(documentID.strip()!= "1337133713371337"):
            data = {"message" : documentID,"signed_hash" : decryptedDocumentID,"status" : "Congratulations"}
            print "Congratulations!!"
        else:
            data = {"message" : documentID,"signed_hash" : decryptedDocumentID,"status" : "Failed!"}
            print "documentID = ",documentID
            print "decryptedDocumentID = ", decryptedDocumentID
            print "Oops!!"
    else:
        data = {"message" : documentID,"signed_hash" : decryptedDocumentID,"status" : "Failed!"}
        print "documentID = ",documentID
        print "decryptedDocumentID = ", decryptedDocumentID
        print "Oops!!"
    return makejson(data)


if __name__ == "__main__":
    app.secret_key = "secret key"
    app.run('0.0.0.0', port=5000)
