
import java.util.*;

public class JSON
{

    public static Map<String, Object> parse(String json) 
    {
        Object value = parseValue(new Tokenizer(json));
        if (value instanceof Map) 
        {
            return (Map<String, Object>) value;
        }
        throw new IllegalArgumentException("Parsed JSON value is not an object");
    }

    private static Object parseValue(Tokenizer tokenizer) 
    {
        if (!tokenizer.hasNext()) return null;

        char c = tokenizer.peek();
        if (c == '{') return parseObject(tokenizer);
        if (c == '\"') return parseString(tokenizer);
        if (Character.isDigit(c)) return parseNumber(tokenizer);
        return null;
    }

    private static Map<String, Object> parseObject(Tokenizer tokenizer) 
    {
        Map<String, Object> result = new HashMap<>();

        tokenizer.next();  // Consume '{'
        while (tokenizer.hasNext() && tokenizer.peek() != '}') 
        {
            String key = parseString(tokenizer);
            
            if(!tokenizer.hasNext() || tokenizer.peek() != ':') 
            {
                throw new IllegalArgumentException("Expected ':' after key in JSON object");
            }

            tokenizer.next();  // Consume ':'
            Object value = parseValue(tokenizer);
            result.put(key, value);

            if (tokenizer.hasNext() && tokenizer.peek() == ',') 
            {
                tokenizer.next();  // Consume ','
            }
        }
        
        if(!tokenizer.hasNext()) 
        {
            throw new IllegalArgumentException("Unterminated JSON object");
        }

        tokenizer.next();  // Consume '}'

        return result;
    }


    private static String parseString(Tokenizer tokenizer) 
    {
        StringBuilder sb = new StringBuilder();

        if(!tokenizer.hasNext() || tokenizer.peek() != '\"') 
        {
            throw new IllegalArgumentException("Expected '\"' at the start of JSON string");
        }

        tokenizer.next();  // Consume '\"'
        
        while (tokenizer.hasNext() && tokenizer.peek() != '\"') 
        {
            sb.append(tokenizer.next());
        }

        if(!tokenizer.hasNext()) 
        {
            throw new IllegalArgumentException("Unterminated JSON string");
        }

        tokenizer.next();  // Consume '\"'
        
        return sb.toString();
    }


    private static Object parseNumber(Tokenizer tokenizer) 
    {
        StringBuilder sb = new StringBuilder();
        while (tokenizer.hasNext() && "0123456789".indexOf(tokenizer.peek()) >= 0) 
        {
            sb.append(tokenizer.next());
        }
        return Integer.parseInt(sb.toString());
    }

    private static class Tokenizer 
    {
        private final String str;
        private int pos = 0;

        public Tokenizer(String str) 
        {
            this.str = str;
        }

        public char next() 
        {
            if (pos < str.length()) 
            {
                return str.charAt(pos++);
            }
            throw new IllegalArgumentException("Unexpected end of JSON");
        }

        public char peek() 
        {
            skipWhitespace();
            if (pos < str.length()) 
            {
                return str.charAt(pos);
            }
            throw new IllegalArgumentException("Unexpected end of JSON");
        }

        public boolean hasNext() 
        {
            skipWhitespace();
            return pos < str.length();
        }

        private void skipWhitespace() 
        {
            while (pos < str.length() && Character.isWhitespace(str.charAt(pos))) 
            {
                pos++;
            }
        }
    }


    public static void main(String[] args) 
    {
        String input = "{ \"debug\" : \"on\", \"window\" : { \"title\" : \"sample\", \"size\": 500 } }";
        Map<String, Object> output = JSON.parse(input);
        System.out.println(output.get("debug").equals("on"));
        
        Object windowObj = output.get("window");
        if (!(windowObj instanceof Map)) {
            throw new AssertionError("Expected 'window' to be a Map");
        }
        Map<String, Object> windowMap = (Map<String, Object>) windowObj;
        System.out.println(windowMap.get("title"));
        System.out.println(windowMap.get("size"));
        System.out.println(output);
    }
}
