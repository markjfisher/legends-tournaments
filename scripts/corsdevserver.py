#!/usr/bin/python

import BaseHTTPServer
import cgi
from pprint import pformat

PORT = 8000
FILE_TO_SERVE = '/Users/mark.fisher/dev/gaming/static/getThing.json'


class MyHandler(BaseHTTPServer.BaseHTTPRequestHandler):
    """
    For more information on CORS see:
    * https://developer.mozilla.org/en-US/docs/HTTP/Access_control_CORS
    * http://enable-cors.org/
    """
    def do_OPTIONS(self):
        self.send_response(200, "ok")
        self.send_header('Access-Control-Allow-Credentials', 'true')
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT')
        self.send_header("Access-Control-Allow-Headers", "X-Requested-With, Content-type")

    def do_POST(self, *args, **kwargs):
        ctype, pdict = cgi.parse_header(self.headers.getheader('content-type'))
        print self.headers
        print cgi.FieldStorage()
        postvars = {}
        try:
            if ctype == 'text/xml':
                length = int(self.headers.getheader('content-length'))
            elif ctype == 'application/json':
                length = int(self.headers.getheader('content-length'))
                data = self.rfile.read(int(length))
                print 'got data: ' + data
            elif ctype == 'multipart/form-data':
                postvars = cgi.parse_multipart(self.rfile, pdict)
            elif ctype == 'application/x-www-form-urlencoded':
                length = int(self.headers.getheader('content-length'))
                postvars = cgi.parse_qs(self.rfile.read(length), keep_blank_values=1)
            else:
                postvars = {}

            print 'post variables:'
            print pformat(postvars)

            body = ''
            with open(FILE_TO_SERVE) as f:
                body = f.read()

            # set headers
            self.send_response(200)
            self.send_header('Access-Control-Allow-Credentials', 'true')
            self.send_header('Access-Control-Allow-Origin', '*')
            self.send_header("Content-type", "application/json")
            self.send_header("Content-length", str(len(body)))
            self.end_headers()

            self.wfile.write(body)
            self.wfile.close()
        except Exception, e:
            print e

    def do_PUT(self, *args, **kwargs):
        self.do_POST(args, kwargs)

    def do_GET(self, *args, **kwargs):
        """ just for testing """
        self.send_response(200)
        self.send_header("Content-type", "application/json")
        self.send_header('Access-Control-Allow-Origin', '*')
        self.end_headers()
        body = ''
        with open(FILE_TO_SERVE) as f:
            body = f.read()
        self.wfile.write(body)
        self.wfile.close()


def httpd(handler_class=MyHandler, server_address=('0.0.0.0', PORT), file_=None):
    try:
        print "Server started on http://%s:%s/ serving file %s" % (server_address[0], server_address[1], FILE_TO_SERVE)
        srvr = BaseHTTPServer.HTTPServer(server_address, handler_class)
        srvr.serve_forever()  # serve_forever
    except KeyboardInterrupt:
        srvr.socket.close()


if __name__ == "__main__":
    """ ./corsdevserver.py """
    httpd()
