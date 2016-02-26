package framework.mina.chat.demo;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineDecoder;
import org.apache.mina.filter.codec.textline.TextLineEncoder;

import java.nio.charset.Charset;

//编解码工厂
public  class CodecFactory implements ProtocolCodecFactory {

        public ProtocolEncoder getEncoder(IoSession session) throws Exception {
            TextLineEncoder coder = new TextLineEncoder(Charset.forName("utf-8"), new LineDelimiter("\n"));
            coder.setMaxLineLength(Integer.MAX_VALUE);
            return coder;
        }

        public ProtocolDecoder getDecoder(IoSession session) throws Exception {
            TextLineDecoder coder = new TextLineDecoder(Charset.forName("utf-8"), new LineDelimiter("\n"));
            coder.setMaxLineLength(Integer.MAX_VALUE);
            return coder;
        }
    }