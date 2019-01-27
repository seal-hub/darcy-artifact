package net.leo.message.base.lang;

import static net.leo.message.base.lang.Sex.FEMALE;
import static net.leo.message.base.lang.Sex.MALE;
import static net.leo.message.base.lang.Sex.TRANS;

public enum Character {

	/**
	 * 鬼姐
	 */
	C01("老鬼", FEMALE),
	/**
	 * 槍叔
	 */
	C02("老槍", MALE),
	/**
	 * 金胖子
	 */
	C03("老金", MALE),
	/**
	 * 耳機
	 */
	C04("譯電員", FEMALE),
	/**
	 * 玫瑰
	 */
	C05("黑玫瑰", FEMALE),
	/**
	 * 30
	 */
	C06("閃靈", FEMALE),
	/**
	 * 鋼鐵
	 */
	C07("鋼鐵特工", MALE),
	/**
	 * 浮萍
	 */
	C08("浮萍", FEMALE),
	/**
	 * 峨嵋老頭
	 */
	C09("峨嵋風", MALE),
	/**
	 * 黃雀女神
	 */
	C10("黃雀", FEMALE),
	/**
	 * 情報處長
	 */
	C11("情報處長", MALE),
	/**
	 * 小白
	 */
	C12("小白", TRANS),
	/**
	 * 大醜女
	 */
	C13("大美女", FEMALE),
	/**
	 * 66
	 */
	C14("六姐", FEMALE),
	/**
	 * 殺手
	 */
	C15("職業殺手", MALE),
	/**
	 * 馬哥
	 */
	C16("小馬哥", MALE),
	/**
	 * 戴老闆
	 */
	C17("戴笠", MALE),
	/**
	 * 摩斯
	 */
	C18("福爾摩斯", MALE),
	/**
	 * 蛇
	 */
	C19("蝮蛇", MALE),
	/**
	 * 帽子
	 */
	C20("貝雷帽", MALE),
	/**
	 * 700
	 */
	C21("柒佰", MALE),
	/**
	 * 禮服
	 */
	C22("禮服蒙面人", MALE),
	/**
	 * 99
	 */
	C23("怪盜九九", MALE),
	/**
	 * 刀鋒
	 */
	C24("刀鋒", MALE),
	/**
	 * 叛徒
	 */
	C25("致命香水", FEMALE);

	public static Character getInstanceByName(String name) {
		for (Character chr : values()) {
			if (chr.name.endsWith(name)) {
				return chr;
			}
		}
		return null;
	}

	private final String name;
	private final Sex sex;

	Character(String name, Sex sex) {
		this.name = name;
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public Sex getSex() {
		return sex;
	}
}
