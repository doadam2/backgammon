<?php

// File and rotation

for($i = 1; $i < 16; $i++)
{
$filename = 'whitep'.$i.'bk.png';
$degrees = 180;

// Content type
//header('Content-type: image/png');

// Load
$source = imagecreatefrompng($filename);

// Rotate
$rotate = imagerotate($source, $degrees, 0);

$transparent = imagecolorallocatealpha($rotate, 255, 255, 255, 0);
imagealphablending($rotate, false);
imagesavealpha($rotate,true);

// Output
imagepng($rotate, 'whitep'.$i.'tk.png');
imagedestroy($rotate);
}
?>
