public class Base32 extends Cipher {
	private final static char[] conversion = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f'};
	public Base32(String text, String key) 
	{
		super(text, key);	
	}
	public void Base32Encode()
	{
		byte[] bytes = this.text.getBytes();
		StringBuilder binary = new StringBuilder();
		for (byte b : bytes)
		{
			int val = b;
			for (int i = 0; i < 8; i++)
			{
			binary.append((val & 128) == 0 ? 0 : 1);
			val <<= 1;
			}
		}
		while (binary.length() % 5 != 0)
		{
			binary.append('0');
		}
		StringBuilder base32 = new StringBuilder();
		for (int j = 0; j < (binary.length() / 5); j++)
		{
			base32.append(conversion[Integer.parseInt(binary.substring(j*5, (j+1)*5), 2)]);
		}
		this.text = base32.toString();
	}
	public void Base32Decode()
	{
		char[] textarray = text.toCharArray();
      int conversionvalue;
		StringBuilder binary = new StringBuilder();
		for (int i = 0; i < text.length(); i++)
		{
			conversionvalue = String.valueOf(conversion).indexOf(textarray[i]);
         if (Integer.toBinaryString(conversionvalue).length() < 5)
         {
            for (int j = 0; j < (5 - Integer.toBinaryString(conversionvalue).length()); j++)
            {
               binary.append("0");
            }
         }
         binary.append(Integer.toBinaryString(conversionvalue));
		}
		StringBuilder normaltext = new StringBuilder();
		for (int j = 0; j < (binary.length() / 8); j++)
		{
			int c = Integer.parseInt(binary.substring(j*8, (j+1)*8), 2);
			normaltext.append((char)c);
		}
		this.text = normaltext.toString();
	}
}
