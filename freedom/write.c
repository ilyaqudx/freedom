/*Socket.writeBegin = function(self,cmd,ver,subVer,deviceType)
   66:  return socket_write_begin(self.m_socketType,cmd,ver,subVer,deviceType);
   67  end*/

signed int __fastcall socket_write_begin(const char *a1)
{
  const char *v1; // r4@1
  const char *v2; // r0@3
  const char *v3; // r1@3
  CSocketManager *v4; // r0@4
  CGlobal *v5; // r0@7
  int v6; // r0@7
  int v7; // r5@7
  int v8; // r0@8
  CGlobal *v9; // r0@8

  v1 = a1;
  if ( !a1 || !*a1 )
  {
    v2 = "socket";
    v3 = "socket_write_begin null name";
LABEL_6:
    socket_log(v2, v3);
    return -1;
  }
  v4 = (CSocketManager *)CSingleton<CSocketManager,CreateUsingNew>::Instance();
  if ( !CSocketManager::GetSocketByName(v4, v1) )
  {
    v2 = "socket";
    v3 = "write begin non-connected\n";
    goto LABEL_6;
  }
  v5 = (CGlobal *)((int (*)(void))CSingleton<CGlobal,CreateUsingNew>::Instance)();
  v6 = CGlobal::AllocPacket(v5, v1);
  v7 = v6;
  if ( v6 )
  {
    v8 = (*(int (__cdecl **)(int))(*(_DWORD *)v6 + 16))(v6);
    v9 = (CGlobal *)CSingleton<CGlobal,CreateUsingNew>::Instance(v8);
    CGlobal::AddPacket(v9, *(_DWORD *)(v7 + 4), (CDataPacket *)v7);
    return *(_DWORD *)(v7 + 4);
  }
  return -1;
}




int __fastcall CSingleton<CGlobal,CreateUsingNew>::Instance(int a1, int a2, int a3)
{
  int v3; // r5@3
  int v5; // [sp+0h] [bp-18h]@1
  int v6; // [sp+4h] [bp-14h]@1
  int v7; // [sp+8h] [bp-10h]@1

  v5 = a1;
  v6 = a2;
  v7 = a3;
  if ( !CSingleton<CGlobal,CreateUsingNew>::_instance )
  {
    CAutoLock::CAutoLock(&v5, &CSingleton<CGlobal,CreateUsingNew>::_lock);
    if ( !CSingleton<CGlobal,CreateUsingNew>::_instance )
    {
      v3 = operator new(0x98u);
      sub_9FF78();
      CSingleton<CGlobal,CreateUsingNew>::_instance = v3;
    }
    CAutoLock::~CAutoLock((CAutoLock *)&v5);
  }
  return CSingleton<CGlobal,CreateUsingNew>::_instance;
}


int __fastcall sub_9FF78(int a1)
{
  int v1; // r4@1
  int v2; // r1@1
  int v3; // r5@1
  int v4; // r3@1
  int v5; // r1@1
  int v6; // r2@1
  int v7; // r3@1
  int v9; // [sp+0h] [bp-10h]@1
  int v10; // [sp+10h] [bp+0h]@1
  int v11; // [sp+14h] [bp+4h]@1
  int v12; // [sp+18h] [bp+8h]@1
  char s; // [sp+20h] [bp+10h]@1

  v1 = a1;
  memset(&v9, 0, 0x10u);
  memcpy(&v10, &v9, 0x10u);
  v2 = v11;
  v3 = v12;
  *(_DWORD *)v1 = v10;
  *(_DWORD *)(v1 + 4) = v2;
  *(_DWORD *)(v1 + 8) = v3;
  *(_BYTE *)v1 = 0;
  *(_DWORD *)(v1 + 4) = 0;
  *(_DWORD *)(v1 + 16) = 0;
  *(_DWORD *)(v1 + 8) = v1;
  *(_DWORD *)(v1 + 12) = v1;
  CThreadLock::CThreadLock(v1 + 24);
  memset(&s, 0, 0x10u);
  memcpy(&v10, &s, 0x10u);
  v4 = v1 + 32;
  v5 = v11;
  v6 = v12;
  *(_DWORD *)v4 = v10;
  *(_DWORD *)(v4 + 4) = v5;
  *(_DWORD *)(v4 + 8) = v6;
  *(_BYTE *)(v1 + 32) = 0;
  *(_DWORD *)(v1 + 36) = 0;
  *(_DWORD *)(v1 + 40) = v1 + 32;
  *(_DWORD *)(v1 + 44) = v1 + 32;
  *(_DWORD *)(v1 + 48) = 0;
  *(_DWORD *)(v1 + 56) = 0;
  *(_DWORD *)(v1 + 60) = 0;
  *(_DWORD *)(v1 + 64) = 0;
  *(_DWORD *)(v1 + 68) = 0;
  *(_DWORD *)(v1 + 72) = 0;
  *(_DWORD *)(v1 + 76) = 0;
  *(_DWORD *)(v1 + 80) = 0;
  *(_DWORD *)(v1 + 84) = 0;
  *(_DWORD *)(v1 + 88) = 0;
  *(_DWORD *)(v1 + 92) = 0;
  std::priv::_Deque_base<_CSocketEventData,std::allocator<_CSocketEventData>>::_M_initialize_map(v1 + 56, 0);
  CThreadLock::CThreadLock(v1 + 96);
  *(_DWORD *)(v1 + 104) = 0;
  *(_DWORD *)(v1 + 108) = 0;
  *(_DWORD *)(v1 + 112) = 0;
  *(_DWORD *)(v1 + 116) = 0;
  *(_DWORD *)(v1 + 120) = 0;
  *(_DWORD *)(v1 + 124) = 0;
  *(_DWORD *)(v1 + 128) = 0;
  *(_DWORD *)(v1 + 132) = 0;
  v7 = v1 + 136;
  *(_DWORD *)v7 = 0;
  *(_DWORD *)(v7 + 4) = 0;
  std::priv::_Deque_base<_CSocketEventData,std::allocator<_CSocketEventData>>::_M_initialize_map(v1 + 104, 0);
  CThreadLock::CThreadLock(v1 + 144);
  return v1;
}


int __fastcall CGlobal::AllocPacket(CGlobal *this, const char *a2)
{
  const char *v2; // r6@1
  CGlobal *v3; // r7@1
  int v4; // r0@1
  int v5; // r5@2
  CSocketManager *v6; // r0@3
  int v7; // r4@3
  size_t v8; // r0@5
  const char *v9; // r0@6
  const char *v10; // r1@6
  CSocketManager *v11; // r0@9
  const char *v12; // r0@10
  signed int v13; // r3@11
  const char *v14; // r0@12
  signed int v15; // r3@14
  const char *v16; // r0@16
  const char *v17; // r0@18
  const char *v18; // r0@20
  const char *v19; // r0@22
  int v20; // r0@27
  int v21; // r3@27
  int v22; // r2@27
  int i; // [sp+14h] [bp-24h]@1
  char v25; // [sp+18h] [bp-20h]@1

  v2 = a2;
  v3 = this;
  v4 = CAutoLock::CAutoLock(&v25, (char *)this + 24);
  for ( i = *((_DWORD *)v3 + 10); (CGlobal *)i != (CGlobal *)((char *)v3 + 32); v4 = sub_A2F1C(&i) )
  {
    v5 = *(_DWORD *)(i + 20);
    if ( !*(_BYTE *)(v5 + 8) )
    {
      v6 = (CSocketManager *)((int (*)(void))CSingleton<CSocketManager,CreateUsingNew>::Instance)();
      v7 = CSocketManager::GetSocketByName(v6, (const char *)(v5 + 28));
      if ( v7 )
      {
        v8 = strlen(v2);
        if ( !strncmp(v2, *(const char **)(v7 + 120), v8) )
        {
          *(_BYTE *)(v5 + 8) = 1;
          v9 = "socket";
          v10 = "alloc packet reused:%s id[%d]\n";
          goto LABEL_28;
        }
      }
      else
      {
        socket_log("socket", "alloc packet error:%s dpown[%s]\n", v2, v5 + 28);
      }
    }
  }
  v11 = (CSocketManager *)CSingleton<CSocketManager,CreateUsingNew>::Instance(v4);
  v5 = CSocketManager::GetSocketByName(v11, v2);
  if ( !v5 )
    goto LABEL_29;
  v12 = (const char *)CGlobal::GetSocketProtocolType(v3, v2);
  if ( !strncmp("BY9", v12, 4u) )
  {
    v5 = operator new(0x64u);
    CDataPacket::CDataPacket();
    *(_DWORD *)v5 = &off_C96A8;
    v13 = 9;
  }
  else
  {
    v14 = (const char *)CGlobal::GetSocketProtocolType(v3, v2);
    if ( !strncmp("BY14", v14, 5u) )
    {
      v5 = operator new(0x64u);
      CDataPacket::CDataPacket();
      *(_DWORD *)v5 = &off_C9560;
      v13 = 14;
    }
    else
    {
      v16 = (const char *)CGlobal::GetSocketProtocolType(v3, v2);
      if ( !strncmp("QE", v16, 3u) )
      {
        v5 = operator new(0x64u);
        CDataPacket::CDataPacket();
        *(_DWORD *)v5 = &off_C9728;
        *(_DWORD *)(v5 + 48) = *(_DWORD *)(v5 + 52) + 15;
        v15 = 4;
        goto LABEL_15;
      }
      v17 = (const char *)CGlobal::GetSocketProtocolType(v3, v2);
      if ( !strncmp("TEXAS", v17, 6u) )
      {
        v5 = operator new(0x64u);
        CDataPacket::CDataPacket();
        *(_DWORD *)(v5 + 48) = 13;
        *(_DWORD *)v5 = &off_C9470;
        *(_DWORD *)(v5 + 56) = 0;
        socket_log("socket", " CDataPacket_Texas::headsize:%d\n");
        goto LABEL_25;
      }
      v18 = (const char *)CGlobal::GetSocketProtocolType(v3, v2);
      if ( !strncmp("VOICE", v18, 6u) )
      {
        v5 = operator new(0x64u);
        CDataPacket::CDataPacket();
        *(_DWORD *)(v5 + 56) = 0;
        *(_DWORD *)v5 = &off_C9640;
        *(_DWORD *)(v5 + 48) = 15;
        goto LABEL_25;
      }
      v19 = (const char *)CGlobal::GetSocketProtocolType(v3, v2);
      if ( strncmp("BY7", v19, 4u) )
      {
        v5 = operator new(0x64u);
        CDataPacket::CDataPacket();
        goto LABEL_25;
      }
      v5 = operator new(0x64u);
      CDataPacket::CDataPacket();
      *(_DWORD *)v5 = &off_C95D8;
      v13 = 7;
    }
  }
  *(_DWORD *)(v5 + 48) = v13;
  v15 = 2;
LABEL_15:
  *(_DWORD *)(v5 + 56) = v15;
LABEL_25:
  if ( !v5 )
  {
    socket_log("socket", "alloc packet error:%s\n", v2);
    goto LABEL_29;
  }
  snprintf((char *)(v5 + 28), 0x13u, "%s", v2);
  *(_BYTE *)(v5 + 16) = CGlobal::GetSocketProtocolEndian(v3, v2);
  v20 = CGlobal::GetHeaderExtend(v3, v2);
  v21 = *(_DWORD *)(v5 + 48);
  *(_DWORD *)(v5 + 52) = v20;
  *(_DWORD *)(v5 + 48) = v21 + v20;
  v22 = dword_CA684;
  *(_DWORD *)(v5 + 4) = dword_CA684;
  dword_CA684 = v22 + 1;
  CDataPacket::reset((CDataPacket *)v5);
  *(_BYTE *)(v5 + 8) = 1;
  *(_DWORD *)sub_A37C2((char *)v3 + 32, v5 + 4) = v5;
  v10 = "alloc packet new:%s id[%d]\n";
  v9 = "socket";
LABEL_28:
  socket_log(v9, v10, v2, *(_DWORD *)(v5 + 4));
LABEL_29:
  CAutoLock::~CAutoLock((CAutoLock *)&v25);
  return v5;
}


int __fastcall CGlobal::AddPacket(CGlobal *this, int a2, CDataPacket *a3)
{
  int v3; // r5@1
  CGlobal *v4; // r4@1
  CDataPacket *v5; // r6@1
  int v7; // [sp+0h] [bp-28h]@1
  int v8; // [sp+8h] [bp-20h]@1
  CDataPacket *v9; // [sp+Ch] [bp-1Ch]@1
  char v10; // [sp+10h] [bp-18h]@1

  v3 = a2;
  v4 = this;
  v5 = a3;
  CAutoLock::CAutoLock(&v7, (char *)this + 24);
  v8 = v3;
  v9 = v5;
  std::priv::_Rb_tree<int,std::less<int>,std::pair<int const,CDataPacket *>,std::priv::_Select1st<std::pair<int const,CDataPacket *>>,std::priv::_MapTraitsT<std::pair<int const,CDataPacket *>>,std::allocator<std::pair<int const,CDataPacket *>>>::insert_unique(
    &v10,
    (char *)v4 + 32,
    &v8);
  return CAutoLock::~CAutoLock((CAutoLock *)&v7);
}

int __fastcall CDataPacket::CDataPacket(int a1)
{
  int v1; // r4@1
  int v2; // r0@1

  v1 = a1;
  *(_DWORD *)a1 = &off_C94E8;
  CTypeConvert::CTypeConvert(a1 + 12);
  *(_DWORD *)(v1 + 92) = 0x2000;
  v2 = operator new[](0x2000u);
  *(_DWORD *)(v1 + 72) = 0;
  *(_DWORD *)(v1 + 76) = 0;
  *(_DWORD *)(v1 + 48) = 0;
  *(_DWORD *)(v1 + 56) = 0;
  *(_DWORD *)(v1 + 52) = 0;
  *(_DWORD *)(v1 + 64) = -1;
  *(_DWORD *)(v1 + 88) = v2;
  *(_BYTE *)(v1 + 60) = 1;
  CDataPacket::reset((CDataPacket *)v1);
  return v1;
}
