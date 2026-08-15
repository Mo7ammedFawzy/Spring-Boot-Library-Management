<template>
  <div
    class="pointer-events-none absolute inset-0 select-none overflow-hidden"
    aria-hidden="true"
  >
    <svg
      class="h-full w-full object-cover md:object-contain md:object-left"
      viewBox="0 0 900 1000"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      preserveAspectRatio="xMinYMid slice"
    >
      <defs>
        <!-- Soft Warm Gradients -->
        <linearGradient
          id="wallWashReg"
          x1="0"
          y1="0"
          x2="1"
          y2="1"
        >
          <stop
            offset="0%"
            stop-color="#faf7f2"
            stop-opacity="0.9"
          />
          <stop
            offset="100%"
            stop-color="#f5efe6"
            stop-opacity="0.4"
          />
        </linearGradient>

        <!-- Teal Lounge Armchair Gradients -->
        <linearGradient
          id="chairTealMain"
          x1="0"
          y1="0"
          x2="0"
          y2="1"
        >
          <stop
            offset="0%"
            stop-color="#8bb1ac"
          />
          <stop
            offset="45%"
            stop-color="#699690"
          />
          <stop
            offset="100%"
            stop-color="#4d7973"
          />
        </linearGradient>

        <linearGradient
          id="chairCushion"
          x1="0"
          y1="0"
          x2="1"
          y2="1"
        >
          <stop
            offset="0%"
            stop-color="#9dc1bc"
          />
          <stop
            offset="60%"
            stop-color="#729f99"
          />
          <stop
            offset="100%"
            stop-color="#55817c"
          />
        </linearGradient>

        <linearGradient
          id="chairWoodLegs"
          x1="0"
          y1="0"
          x2="1"
          y2="1"
        >
          <stop
            offset="0%"
            stop-color="#9c826c"
          />
          <stop
            offset="100%"
            stop-color="#664e3b"
          />
        </linearGradient>

        <linearGradient
          id="tableWood"
          x1="0"
          y1="0"
          x2="0"
          y2="1"
        >
          <stop
            offset="0%"
            stop-color="#f0eae1"
          />
          <stop
            offset="100%"
            stop-color="#ddd2c4"
          />
        </linearGradient>

        <linearGradient
          id="pendantLight"
          x1="0"
          y1="0"
          x2="0"
          y2="1"
        >
          <stop
            offset="0%"
            stop-color="#fff5e6"
            stop-opacity="0.5"
          />
          <stop
            offset="100%"
            stop-color="#fffbf5"
            stop-opacity="0"
          />
        </linearGradient>

        <filter
          id="chairShadow"
          x="-15%"
          y="-15%"
          width="130%"
          height="140%"
        >
          <feDropShadow
            dx="2"
            dy="10"
            stdDeviation="8"
            flood-color="#3c524e"
            flood-opacity="0.14"
          />
        </filter>
      </defs>

      <!-- 1. Background Library Bookshelves & Architecture -->
      <g
        stroke="#d8cebf"
        stroke-width="1.2"
        stroke-linecap="round"
        stroke-linejoin="round"
        opacity="0.85"
      >
        <!-- Distant Architectural Ceiling & Mouldings -->
        <line
          x1="0"
          y1="80"
          x2="500"
          y2="80"
          stroke="#eae3da"
          stroke-width="1.5"
        />
        <line
          x1="0"
          y1="92"
          x2="500"
          y2="92"
          stroke="#f0eae2"
          stroke-width="1"
        />

        <!-- Vertical Bookshelf Uprights -->
        <line
          x1="15"
          y1="120"
          x2="15"
          y2="680"
          stroke-width="2"
          stroke="#cbbdb0"
        />
        <line
          x1="80"
          y1="120"
          x2="80"
          y2="680"
          stroke-width="1.6"
          stroke="#d5c8bb"
        />
        <line
          x1="170"
          y1="120"
          x2="170"
          y2="600"
          stroke-width="1.2"
          stroke="#e0d6ca"
        />
        <line
          x1="250"
          y1="120"
          x2="250"
          y2="580"
          stroke-width="1.2"
          stroke="#e4dbd1"
        />

        <!-- Distant Arch & Library Background -->
        <path
          d="M 180 500 L 180 260 C 180 180 270 180 270 260 L 270 500"
          stroke="#e8e0d5"
          fill="none"
          stroke-width="1"
        />

        <!-- Shelf Rows & Books (Upper Section) -->
        <rect
          x="15"
          y="190"
          width="235"
          height="5"
          fill="#efe9e0"
        />
        <!-- Books bay 1 -->
        <rect
          x="20"
          y="130"
          width="8"
          height="60"
          fill="#ded4c8"
        />
        <rect
          x="30"
          y="140"
          width="11"
          height="50"
          fill="#d3c7ba"
        />
        <rect
          x="43"
          y="125"
          width="7"
          height="65"
          fill="#e7ded4"
        />
        <rect
          x="52"
          y="135"
          width="12"
          height="55"
          fill="#dbd0c3"
        />
        <rect
          x="66"
          y="130"
          width="9"
          height="60"
          fill="#e3d9ce"
        />
        <!-- Books bay 2 -->
        <rect
          x="85"
          y="135"
          width="14"
          height="55"
          fill="#d9cec1"
        />
        <rect
          x="101"
          y="125"
          width="8"
          height="65"
          fill="#e5ded3"
        />
        <rect
          x="111"
          y="140"
          width="10"
          height="50"
          fill="#dfd5c9"
        />
        <rect
          x="123"
          y="130"
          width="13"
          height="60"
          fill="#d4c9bc"
        />
        <rect
          x="138"
          y="138"
          width="9"
          height="52"
          fill="#e8e0d6"
        />
        <rect
          x="149"
          y="128"
          width="12"
          height="62"
          fill="#ded5c8"
        />

        <!-- Shelf 2 -->
        <rect
          x="15"
          y="270"
          width="235"
          height="5"
          fill="#efe9e0"
        />
        <rect
          x="20"
          y="205"
          width="12"
          height="65"
          fill="#d5cabb"
        />
        <rect
          x="34"
          y="215"
          width="8"
          height="55"
          fill="#e6ded4"
        />
        <rect
          x="44"
          y="200"
          width="14"
          height="70"
          fill="#ded4c7"
        />
        <rect
          x="60"
          y="210"
          width="10"
          height="60"
          fill="#d3c6b8"
        />
        <rect
          x="85"
          y="210"
          width="9"
          height="60"
          fill="#e3dad0"
        />
        <rect
          x="96"
          y="205"
          width="13"
          height="65"
          fill="#dbd0c4"
        />
        <rect
          x="111"
          y="220"
          width="7"
          height="50"
          fill="#ede5dc"
        />
        <rect
          x="120"
          y="208"
          width="15"
          height="62"
          fill="#d8cec2"
        />

        <!-- Shelf 3 -->
        <rect
          x="15"
          y="350"
          width="235"
          height="5"
          fill="#efe9e0"
        />
        <rect
          x="20"
          y="290"
          width="10"
          height="60"
          fill="#e0d6ca"
        />
        <rect
          x="32"
          y="280"
          width="13"
          height="70"
          fill="#d7ccc0"
        />
        <rect
          x="47"
          y="295"
          width="8"
          height="55"
          fill="#e9e1d8"
        />
        <rect
          x="57"
          y="285"
          width="11"
          height="65"
          fill="#dacfc3"
        />
        <rect
          x="85"
          y="285"
          width="12"
          height="65"
          fill="#d5cabb"
        />
        <rect
          x="99"
          y="295"
          width="8"
          height="55"
          fill="#e8dfd5"
        />
        <rect
          x="109"
          y="282"
          width="14"
          height="68"
          fill="#ded4c8"
        />

        <!-- Shelf 4 -->
        <rect
          x="15"
          y="430"
          width="235"
          height="5"
          fill="#efe9e0"
        />
        <rect
          x="20"
          y="365"
          width="14"
          height="65"
          fill="#d4c9bc"
        />
        <rect
          x="36"
          y="375"
          width="9"
          height="55"
          fill="#e6ded5"
        />
        <rect
          x="47"
          y="360"
          width="12"
          height="70"
          fill="#dcd2c6"
        />
        <rect
          x="61"
          y="370"
          width="8"
          height="60"
          fill="#dfd6cc"
        />
        <rect
          x="85"
          y="370"
          width="10"
          height="60"
          fill="#d9cebf"
        />
        <rect
          x="97"
          y="365"
          width="13"
          height="65"
          fill="#e5ded4"
        />
        <rect
          x="112"
          y="378"
          width="8"
          height="52"
          fill="#dbd0c4"
        />
      </g>

      <!-- 2. Distant Library Study Table & Chair (Background center) -->
      <g
        stroke="#cfc3b5"
        stroke-width="1.1"
        stroke-linecap="round"
        stroke-linejoin="round"
        opacity="0.8"
      >
        <!-- Distant Table -->
        <polygon
          points="190,480 300,480 290,520 180,520"
          fill="#f5ede3"
        />
        <line
          x1="183"
          y1="520"
          x2="183"
          y2="580"
          stroke-width="1.5"
        />
        <line
          x1="287"
          y1="520"
          x2="287"
          y2="580"
          stroke-width="1.5"
        />
        <line
          x1="210"
          y1="510"
          x2="210"
          y2="560"
          stroke-width="1"
        />
        <line
          x1="270"
          y1="510"
          x2="270"
          y2="560"
          stroke-width="1"
        />

        <!-- Distant Chair -->
        <path
          d="M 235 440 L 255 440 L 255 480 L 235 480 Z"
          fill="#eae1d6"
        />
        <line
          x1="237"
          y1="480"
          x2="237"
          y2="530"
        />
        <line
          x1="253"
          y1="480"
          x2="253"
          y2="530"
        />
      </g>

      <!-- 3. Minimalist Hanging Pendant Lamp from Ceiling -->
      <g
        stroke="#3a3734"
        stroke-linecap="round"
        stroke-linejoin="round"
      >
        <!-- Cord -->
        <line
          x1="230"
          y1="0"
          x2="230"
          y2="230"
          stroke="#5c5752"
          stroke-width="1.5"
        />
        
        <!-- Ceiling Fixture / Rose -->
        <ellipse
          cx="230"
          cy="4"
          rx="8"
          ry="3"
          fill="#3a3734"
        />
        
        <!-- Pendant Shade Cone -->
        <path
          d="M 226 230 L 210 256 C 210 258 250 258 250 256 L 234 230 Z"
          fill="#44413d"
          stroke-width="1.5"
        />
        <!-- Lower Opening Rim & Warm Glow -->
        <ellipse
          cx="230"
          cy="256"
          rx="20"
          ry="6"
          fill="#fff7e6"
          stroke="#3a3734"
          stroke-width="1.4"
        />
        <ellipse
          cx="230"
          cy="256"
          rx="14"
          ry="4"
          fill="#ffffff"
        />
        
        <!-- Soft Light Beam Cone -->
        <polygon
          points="230,258 140,550 320,550"
          fill="url(#pendantLight)"
          opacity="0.4"
        />
      </g>

      <!-- 4. Cozy Teal/Sage Danish Armchair (Main Focal Element) -->
      <g filter="url(#chairShadow)">
        <!-- Chair Wooden Legs (Back & Front) -->
        <g
          stroke="#3d2c1f"
          stroke-width="1.4"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <!-- Back Left Leg -->
          <polygon
            points="48,640 40,710 45,712 55,640"
            fill="url(#chairWoodLegs)"
          />
          <!-- Back Right Leg -->
          <polygon
            points="120,630 132,695 137,693 126,630"
            fill="url(#chairWoodLegs)"
          />
          <!-- Front Left Leg -->
          <polygon
            points="56,660 48,735 55,737 65,660"
            fill="url(#chairWoodLegs)"
          />
          <!-- Front Right Leg -->
          <polygon
            points="135,650 148,725 154,723 143,650"
            fill="url(#chairWoodLegs)"
          />
        </g>

        <!-- Chair Main Body (Upholstery in Soft Sage/Teal) -->
        <g
          stroke="#264742"
          stroke-width="1.8"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <!-- Curved Tufted Backrest & Wings -->
          <path
            d="M 52 490 
               C 50 460, 68 450, 96 450 
               C 124 450, 144 460, 142 490 
               L 146 590 
               C 146 620, 140 640, 126 645 
               L 68 645 
               C 54 640, 48 620, 48 590 
               Z"
            fill="url(#chairTealMain)"
          />

          <!-- Backrest Tufting / Contour Seam Lines -->
          <path
            d="M 75 465 C 75 510, 78 570, 78 600"
            stroke="#3d6c66"
            stroke-width="1.2"
            stroke-dasharray="3,2"
            fill="none"
          />
          <path
            d="M 96 455 C 96 510, 96 570, 96 600"
            stroke="#3d6c66"
            stroke-width="1.2"
            stroke-dasharray="3,2"
            fill="none"
          />
          <path
            d="M 117 465 C 117 510, 114 570, 114 600"
            stroke="#3d6c66"
            stroke-width="1.2"
            stroke-dasharray="3,2"
            fill="none"
          />

          <!-- Left Armrest Flare -->
          <path
            d="M 48 530 C 35 550, 34 585, 48 625 C 56 632, 65 630, 65 615 C 56 580, 56 555, 60 535 Z"
            fill="#7ba5a0"
          />

          <!-- Right Armrest Flare -->
          <path
            d="M 144 530 C 157 550, 158 585, 144 625 C 136 632, 127 630, 127 615 C 136 580, 136 555, 132 535 Z"
            fill="#5b8882"
          />

          <!-- Deep Plump Seat Cushion -->
          <path
            d="M 45 605 
               C 45 595, 60 590, 96 590 
               C 132 590, 147 595, 147 605 
               L 145 640 
               C 145 655, 130 660, 96 660 
               C 62 660, 47 655, 47 640 
               Z"
            fill="url(#chairCushion)"
          />
          
          <!-- Cushion Top Highlight Seam -->
          <ellipse
            cx="96"
            cy="605"
            rx="46"
            ry="14"
            fill="#a4c8c3"
            stroke="#396660"
            stroke-width="1.2"
          />
        </g>
      </g>

      <!-- 5. Round Light Wood Side Table & Book Stack -->
      <g filter="url(#chairShadow)">
        <!-- Table Legs -->
        <g
          stroke="#3a2f26"
          stroke-width="1.4"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <line
            x1="175"
            y1="675"
            x2="160"
            y2="755"
            stroke="url(#chairWoodLegs)"
            stroke-width="2.5"
          />
          <line
            x1="225"
            y1="675"
            x2="240"
            y2="755"
            stroke="url(#chairWoodLegs)"
            stroke-width="2.5"
          />
          <line
            x1="190"
            y1="680"
            x2="185"
            y2="770"
            stroke="url(#chairWoodLegs)"
            stroke-width="2.8"
          />
          <line
            x1="210"
            y1="680"
            x2="215"
            y2="770"
            stroke="url(#chairWoodLegs)"
            stroke-width="2.8"
          />
        </g>

        <!-- Round Table Top (Perspective Oval) -->
        <g
          stroke="#2f2720"
          stroke-width="1.6"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <!-- Table Rim Depth -->
          <path
            d="M 160 670 C 160 682, 240 682, 240 670 L 240 678 C 240 690, 160 690, 160 678 Z"
            fill="#cfc3b5"
          />
          <!-- Table Surface -->
          <ellipse
            cx="200"
            cy="670"
            rx="40"
            ry="12"
            fill="url(#tableWood)"
          />
        </g>

        <!-- Stack of 3 Books on Side Table -->
        <g
          stroke="#2b231c"
          stroke-width="1.3"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <!-- Book 1 (Bottom - Wide cream volume) -->
          <polygon
            points="182,658 218,658 222,665 178,665"
            fill="#f0eae1"
          />
          <polygon
            points="178,665 222,665 222,670 178,670"
            fill="#dfd5c8"
          />
          
          <!-- Book 2 (Middle - Terracotta volume) -->
          <polygon
            points="185,650 216,650 219,657 181,657"
            fill="#a44820"
          />
          <polygon
            points="181,657 219,657 219,662 181,662"
            fill="#88350f"
          />
          <line
            x1="184"
            y1="659"
            x2="192"
            y2="659"
            stroke="#d49b55"
            stroke-width="0.8"
          />

          <!-- Book 3 (Top - Light Sage volume) -->
          <polygon
            points="186,643 214,643 217,649 183,649"
            fill="#7fa8a2"
          />
          <polygon
            points="183,649 217,649 217,653 183,653"
            fill="#5f8883"
          />
        </g>
      </g>
    </svg>
  </div>
</template>
