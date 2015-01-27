/**
 * 
 */
package com.snowgoon.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * @author snowgoon88@gmail.com
 *
 */
public class NBRadioButton {
	/** Texture Atlas */
	Texture _textureAtlas;
	
	/** Each different region for the texture */
	TextureRegion[] _textureRegion;
	Rectangle[] _rectIcons;

	final int NB_COLS = 2;
	final int NB_ROWS = 4;
	
	/** Current selection */
	int _nbSelected = 1;
	
	/** Top Left coordinate */
	float _top, _left;
	/**
	 * 
	 */
	public NBRadioButton( float left, float top ) {
		// Load textures Atlas
		_textureAtlas = new Texture(Gdx.files.internal("nb_texturemap.png"));
		// Compute Regions : NB_ROWS rows of NB_COLS columns
		TextureRegion[][] tmp = TextureRegion.split(_textureAtlas, 
				_textureAtlas.getWidth()/NB_COLS,
				_textureAtlas.getHeight()/NB_ROWS);    
        _textureRegion = new TextureRegion[NB_COLS * NB_ROWS];
        int index = 0;
        for (int i = 0; i < NB_COLS; i++) {
            for (int j = 0; j < NB_ROWS; j++) {
                _textureRegion[index++] = tmp[j][i];
            }
        }
        _nbSelected = 1;
        
        // Set coordinate
        _top = top;
        _left = left;
        _rectIcons = new Rectangle[NB_ROWS];
        float iconSize = (float) _textureAtlas.getWidth()/NB_COLS;
        for (int i = 0; i < _rectIcons.length; i++) {
        	_rectIcons[i] = new Rectangle(_left, _top-i*(iconSize+6f), iconSize, iconSize);
		}
	}
	
	/**
	 * Draw icons, if selected, then from second columns
	 * @param spriteBatch : where to render
	 */
	public void render( SpriteBatch spriteBatch ) {
		spriteBatch.begin();
		for (int i = 0; i < _rectIcons.length; i++) {
			if (i == _nbSelected-1) {
				spriteBatch.draw(_textureRegion[i+NB_ROWS],
						_rectIcons[i].x, _rectIcons[i].y,
						_rectIcons[i].width, _rectIcons[i].height);
			}
			else {
				spriteBatch.draw(_textureRegion[i],
						_rectIcons[i].x, _rectIcons[i].y,
						_rectIcons[i].width, _rectIcons[i].height);
			}
		}
		spriteBatch.end();
	}
	
	/**
	 * Check if clicked.
	 */
	public boolean updateFromClick( Vector3 touchPos ) {
		for (int i = 0; i < _rectIcons.length; i++) {
			if (_rectIcons[i].contains(touchPos.x, touchPos.y)) {
				_nbSelected = i+1;
				return true;
			}
		}
		return false;
	}
}
