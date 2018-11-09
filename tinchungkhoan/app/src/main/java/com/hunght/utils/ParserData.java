package com.hunght.utils;

import com.hunght.data.MenuLookUpItemKind;

public class ParserData {
    
	public static String getContent(MenuLookUpItemKind kind) {

        switch (kind)
        {
            case Cafef:
                return "Cafef";
        }
        return "Không xác định được dữ liêu";
    }

}
