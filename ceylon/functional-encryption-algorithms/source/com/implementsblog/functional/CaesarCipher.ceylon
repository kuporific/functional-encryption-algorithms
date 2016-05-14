shared String encrypt(String text, Integer shiftAmount)
{
	return String(
		text
			.map(shiftInRange('a', 'z', shiftAmount))
			.map(shiftInRange('A', 'Z', shiftAmount)));
}

Character(Character) shiftInRange(
		Character lowest, 
		Character highest, 
		Integer shiftAmount)
{
	Integer lowestCodePoint = lowest.integer;
	Integer highestCodePoint = highest.integer;
	Integer distance = highest.integer - lowestCodePoint + 1;
	
	return (Character c) 
	{
		Integer codePoint = c.integer;
		if (lowestCodePoint <= codePoint && codePoint <= highestCodePoint) 
		{ 
			return (((c.integer - lowestCodePoint + shiftAmount) % distance 
				+ distance) % distance + lowestCodePoint)
				.character; 
		}
		else
		{
			return c;
		}
	};
}

shared void test()
{
	print(encrypt("aAzZmy value", -1));
}